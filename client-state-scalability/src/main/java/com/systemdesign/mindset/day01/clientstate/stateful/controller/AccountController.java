package com.systemdesign.mindset.day01.clientstate.stateful.controller;


import com.systemdesign.mindset.day01.clientstate.stateful.dto.BalanceResponse;
import com.systemdesign.mindset.day01.clientstate.stateful.dto.TransferRequest;
import com.systemdesign.mindset.day01.clientstate.stateful.dto.TransferResponse;
import com.systemdesign.mindset.day01.clientstate.stateful.model.User;
import com.systemdesign.mindset.day01.clientstate.stateful.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Account Controller - STATEFUL DESIGN
 * 
 * Tüm endpoint'ler session gerektirir
 * Session yoksa 401 döner
 */
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final UserService userService;

    /**
     * Bakiye Sorgulama - SESSION GEREKLİ
     * 
     * Akış:
     * 1. Session'dan USER_ID al (RAM'den)
     * 2. Database'den bakiye çek
     * 3. Response dön
     */
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(HttpSession session) {
        // 1. Session kontrolü
        Long userId = (Long) session.getAttribute("USER_ID");
        String username = (String) session.getAttribute("USERNAME");

        if (userId == null) {
            log.warn("❌ Unauthorized access attempt to /balance");
            return ResponseEntity.status(401)
                    .body("Unauthorized: Please login first");
        }

        log.info("Balance request from session user: {}", username);

        // 2. Bakiye sorgula
        BigDecimal balance = userService.getBalance(username);

        log.debug("✅ Balance retrieved: {}", balance);

        return ResponseEntity.ok(new BalanceResponse(username, balance));
    }

    /**
     * Transfer İşlemi - SESSION GEREKLİ
     * 
     * Akış:
     * 1. Session'dan USER_ID al
     * 2. Transfer işlemini gerçekleştir
     * 3. Yeni bakiye ile response dön
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(
            @Valid @RequestBody TransferRequest request,
            HttpSession session
    ) {
        // 1. Session kontrolü
        Long userId = (Long) session.getAttribute("USER_ID");
        String username = (String) session.getAttribute("USERNAME");

        if (userId == null) {
            log.warn("❌ Unauthorized transfer attempt");
            return ResponseEntity.status(401)
                    .body("Unauthorized: Please login first");
        }

        log.info("Transfer request: {} -> {} (amount: {})",
                username, request.getToUsername(), request.getAmount());

        try {
            // 2. Transfer işlemi
            userService.transfer(username, request);

            // 3. Yeni bakiye
            BigDecimal newBalance = userService.getBalance(username);

            log.info("✅ Transfer successful. New balance: {}", newBalance);

            return ResponseEntity.ok(new TransferResponse(
                    "Transfer successful",
                    username,
                    request.getToUsername(),
                    request.getAmount(),
                    newBalance
            ));

        } catch (Exception e) {
            log.error("❌ Transfer failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Transfer failed: " + e.getMessage());
        }
    }

    /**
     * Profil bilgisi - SESSION GEREKLİ
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");

        if (username == null) {
            return ResponseEntity.status(401)
                    .body("Unauthorized: Please login first");
        }

        User user = userService.findByUsername(username);

        return ResponseEntity.ok()
                .body(String.format(
                        "Profile: %s (ID: %d) - Balance: %s TL",
                        user.getUsername(),
                        user.getId(),
                        user.getBalance()
                ));
    }
}
