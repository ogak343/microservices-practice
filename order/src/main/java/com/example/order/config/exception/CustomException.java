package com.example.order.config.exception;

import com.example.order.contants.ErrorCode;

public class CustomException extends RuntimeException {
    public CustomException(ErrorCode errorCode) {
        super(errorCode.name());
    }

    public CustomException(String body) {
        super(body);
    }
}
