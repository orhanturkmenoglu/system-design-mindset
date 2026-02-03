package com.systemdesign.mindset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Response DTO
 * Day 02: JWT token döner (Session ID değil!)
 */
public record LoginResponse(
        String token,
        String type,
        String username,
        String message
) {

    public LoginResponse(String token, String username) {
        this(token, "Bearer", username,
                "Login successful - JWT token generated (Stateless!)");
    }

    public LoginResponse(String token, String username, String message) {
        this(token, "Bearer", username, message);
    }
}
