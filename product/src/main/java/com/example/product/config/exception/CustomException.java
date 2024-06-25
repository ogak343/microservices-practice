package com.example.product.config.exception;

import com.example.product.contants.ErrorCode;

public class CustomException extends RuntimeException {
    public CustomException(ErrorCode errorCode) {
        super(errorCode.name());
    }
}
