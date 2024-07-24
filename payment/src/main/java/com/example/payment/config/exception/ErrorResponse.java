package com.example.payment.config.exception;


public record ErrorResponse<T>(
        int code,
        String description,
        T body
) {
}
