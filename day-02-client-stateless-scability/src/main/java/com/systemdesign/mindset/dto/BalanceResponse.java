package com.systemdesign.mindset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Balance Response DTO
 */
public record BalanceResponse(
        String username,
        BigDecimal balance,
        String message
) {

    public BalanceResponse(String username, BigDecimal balance) {
        this(username, balance, "Retrieved from JWT token-based authentication (Stateless!)");
    }
}