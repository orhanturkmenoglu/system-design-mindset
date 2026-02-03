package com.systemdesign.mindset.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record TransferRequest(
        @NotBlank(message = "Target username is required")
        String toUsername,
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1.0", message = "Amount must be at least 1")
        BigDecimal amount
) {
}
