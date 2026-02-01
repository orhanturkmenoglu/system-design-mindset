package com.systemdesign.mindset.day01.clientstate.stateful.controller;


import com.systemdesign.mindset.day01.clientstate.stateful.dto.LoginRequest;
import com.systemdesign.mindset.day01.clientstate.stateful.dto.LoginResponse;
import com.systemdesign.mindset.day01.clientstate.stateful.model.User;
import com.systemdesign.mindset.day01.clientstate.stateful.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller - STATEFUL DESIGN
 * 
 * Day 01: Session-based authentication
 * Server RAM'de session tutar
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    /**
     * Login Endpoint - STATEFUL
     * 
     * Akış:
     * 1. Kullanıcıyı doğrula
     * 2. Session oluştur (RAM'de)
     * 3. Session'a USER_ID kaydet
     * 4. Cookie gönder (JSESSIONID)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        log.info("Login attempt: {}", request.getUsername());

        try {
            // 1. Kullanıcıyı doğrula
            User user = userService.authenticate(
                    request.getUsername(),
                    request.getPassword()
            );

            // 2. Session'a kullanıcı bilgisini kaydet
            // ⚠️ Bu STATEFUL tasarım!
            // Server RAM'de tutacak
            session.setAttribute("USER_ID", user.getId());
            session.setAttribute("USERNAME", user.getUsername());

            log.info("✅ Login successful. Session ID: {}", session.getId());
            log.debug("Session stored in RAM: userId={}", user.getId());

            return ResponseEntity.ok(
                    new LoginResponse(
                            "Login successful",
                            user.getUsername(),
                            "Session ID: " + session.getId().substring(0, 8) + "..."
                    )
            );

        } catch (Exception e) {
            log.error("❌ Login failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(
                            "Login failed: " + e.getMessage(),
                            null,
                            null
                    ));
        }
    }

    /**
     * Logout Endpoint
     * Session'ı invalidate et (RAM'den sil)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        String sessionId = session.getId();

        log.info("Logout: {} (Session: {})", username, sessionId.substring(0, 8));

        // Session'ı sil (RAM'den)
        session.invalidate();

        log.info("✅ Session invalidated (removed from RAM)");

        return ResponseEntity.ok("Logout successful");
    }

    /**
     * Session durumu kontrol
     */
    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo(HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        String username = (String) session.getAttribute("USERNAME");

        if (userId == null) {
            return ResponseEntity.ok()
                    .body("No active session (401 would be returned on protected endpoints)");
        }

        return ResponseEntity.ok()
                .body(String.format(
                        "Active Session - User: %s (ID: %d), Session ID: %s",
                        username,
                        userId,
                        session.getId().substring(0, 8) + "..."
                ));
    }
}
