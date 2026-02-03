package com.systemdesign.mindset.controller;

import com.systemdesign.mindset.dto.LoginRequest;
import com.systemdesign.mindset.dto.LoginResponse;
import com.systemdesign.mindset.security.JwtService;
import com.systemdesign.mindset.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller - STATELESS DESIGN (JWT)
 *
 * Day 02: JWT token oluşturur
 * Day 01 farkı: Session yok, JWT var!
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService; // token-info için  gerekli

    /**
     * Login Endpoint - STATELESS (JWT)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Login attempt: {}", request.username());

        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            log.warn("❌ Login failed for {}: {}", request.username(), e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(
                            null,
                            null,
                            "Login failed: Invalid username or password"
                    ));
        }
    }

    /**
     * Token Info Endpoint (Debug için)
     */
    @GetMapping("/token-info")
    public ResponseEntity<?> getTokenInfo(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.substring(7);

            String username = jwtService.extractUsername(token);
            Long userId = jwtService.extractUserId(token);
            boolean expired = jwtService.isTokenExpired(token);

            return ResponseEntity.ok("""
                    📋 JWT Token Info:
                    - Username: %s
                    - User ID: %d
                    - Expired: %s
                    - Type: Bearer (JWT)

                    ✅ This is STATELESS!
                    Server didn't look up anything in RAM.
                    All info came from the token itself.
                    """.formatted(username, userId, expired));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Invalid token: " + e.getMessage());
        }
    }

    /**
     * Compare with Day 01
     */
    @GetMapping("/compare")
    public ResponseEntity<String> compare() {
        return ResponseEntity.ok("""
            
            ╔════════════════════════════════════════════════════╗
            ║        DAY 01 vs DAY 02 - COMPARISON              ║
            ╚════════════════════════════════════════════════════╝
            
            DAY 01 (STATEFUL):
            ❌ Session ID in cookie
            ❌ Server stores session in RAM
            ❌ Session lost on restart
            ❌ Sticky sessions required
            
            DAY 02 (STATELESS):
            ✅ JWT token (self-contained)
            ✅ Server stores NOTHING
            ✅ Restart-safe
            ✅ Infinite horizontal scaling
            
            🎯 Key Difference:
            Stateful → Server remembers you
            Stateless → You prove yourself (JWT)
            """);
    }
}
