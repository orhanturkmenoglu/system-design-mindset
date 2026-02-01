package com.systemdesign.mindset.day01.clientstate.stateful.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String message;
    private String username;
    private String sessionInfo;
    
    public LoginResponse(String message, String username) {
        this.message = message;
        this.username = username;
        this.sessionInfo = "Session created in server RAM";
    }
}
