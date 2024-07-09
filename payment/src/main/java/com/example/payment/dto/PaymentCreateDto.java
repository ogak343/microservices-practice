package com.example.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCreateDto {

    @NotNull
    private Long orderId;
}
