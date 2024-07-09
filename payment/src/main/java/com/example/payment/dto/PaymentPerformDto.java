package com.example.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentPerformDto {

    @NotNull
    private Long otpId;
    @NotNull
    private Integer otpCode;
}
