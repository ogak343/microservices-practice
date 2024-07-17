package com.example.payment.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentPerformDto (
        @NotNull Long otpId,
        @NotNull Integer otpCode
) {
}
