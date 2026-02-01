package com.systemdesign.mindset.day01.clientstate.stateful.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Balance Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {

    private String username;
    private BigDecimal balance;
    private String message;
    
    public BalanceResponse(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
        this.message = "Retrieved from session-based authentication";
    }
}
