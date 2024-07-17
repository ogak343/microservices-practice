package com.example.product.config.exception;


public record ErrorResponse<T>(
        int code,
        String description,
        T body
) {
}
