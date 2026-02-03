package com.systemdesign.mindset.controller;

import com.systemdesign.mindset.dto.BalanceResponse;
import com.systemdesign.mindset.dto.TransferRequest;
import com.systemdesign.mindset.dto.TransferResponse;
import com.systemdesign.mindset.model.User;
import com.systemdesign.mindset.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Account Controller - STATELESS DESIGN (JWT)
 * <p>
 * Day 02: JWT token'dan user bilgisi alır
 * Day 01 farkı: Session yok, @AuthenticationPrincipal var!
 */
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final UserService userService;

    /**
     * Bakiye Sorgulama - JWT GEREKLİ
     * <p>
     * Akış:
     * 1. JWT Filter token'ı validate eder
     * 2. User authenticate edilir
     * 3. @AuthenticationPrincipal ile user bilgisi gelir
     * 4. Database'den bakiye çekilir
     * <p>
     * ✅ STATELESS: Server RAM'e bakmadı!
     * Sadece JWT'yi doğruladı
     */
    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // ✅ userDetails JWT'den geldi, Session'dan DEĞİL!
        String username = userDetails.getUsername();

        log.info("Balance request from JWT-authenticated user: {}", username);

        BigDecimal balance = userService.getBalance(username);

        log.debug("✅ Balance retrieved: {} (via JWT, not session!)", balance);

        BalanceResponse response =
                new BalanceResponse(username, balance);

        return ResponseEntity.ok(response);
    }

    /**
     * Transfer İşlemi - JWT GEREKLİ
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(
            @Valid @RequestBody TransferRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // ✅ userDetails JWT'den geldi
        String username = userDetails.getUsername();

        log.info("Transfer request from JWT user: {} -> {} (amount: {})",
                username, request.toUsername(), request.amount());

        try {
            // Transfer işlemi
            userService.transfer(username, request);

            // Yeni bakiye
            BigDecimal newBalance = userService.getBalance(username);

            log.info("✅ Transfer successful. New balance: {}", newBalance);

            return ResponseEntity.ok(new TransferResponse(
                    "Transfer successful (processed via JWT auth)",
                    username,
                    request.toUsername(),
                    request.amount(),
                    newBalance
            ));

        } catch (Exception e) {
            log.error("❌ Transfer failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Transfer failed: " + e.getMessage());
        }
    }

    /**
     * Profil bilgisi - JWT GEREKLİ
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);

        return ResponseEntity.ok()
                .body(String.format(
                        """
                                📋 Profile (via JWT):
                                - Username: %s
                                - User ID: %d
                                - Balance: %s TL
                                
                                ✅ Auth method: JWT Token (Stateless)
                                ❌ No session used
                                ✅ Server didn't store anything in RAM
                                """,
                        user.getUsername(),
                        user.getId(),
                        user.getBalance()
                ));
    }

    /**
     * Debug: Auth bilgisi göster
     */
    @GetMapping("/auth-debug")
    public ResponseEntity<String> authDebug(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(String.format(
                """
                        🔍 Authentication Debug:
                        
                        Username: %s
                        Authorities: %s
                        
                        How this works:
                        1. Client sends: Authorization: Bearer <JWT>
                        2. JwtAuthenticationFilter validates token
                        3. User details extracted from JWT
                        4. No RAM lookup needed!
                        5. This is STATELESS authentication
                        
                        Compare with Day 01:
                        - Day 01: HttpSession (RAM lookup)
                        - Day 02: JWT (Token validation)
                        """,
                userDetails.getUsername(),
                userDetails.getAuthorities()
        ));
    }
}
