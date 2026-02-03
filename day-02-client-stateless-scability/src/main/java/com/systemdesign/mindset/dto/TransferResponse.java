package com.systemdesign.mindset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Transfer Response DTO
 */
public record TransferResponse(
        String message,
        String fromUsername,
        String toUsername,
        BigDecimal amount,
        BigDecimal newBalance
) {
}