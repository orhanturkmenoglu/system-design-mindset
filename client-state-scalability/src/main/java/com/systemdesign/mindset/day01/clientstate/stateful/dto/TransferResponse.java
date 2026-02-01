package com.systemdesign.mindset.day01.clientstate.stateful.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Transfer Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private String message;
    private String fromUsername;
    private String toUsername;
    private BigDecimal amount;
    private BigDecimal newBalance;
}
