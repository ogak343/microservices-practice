package com.example.payment.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_STATUS(400),
    INVALID_OTP(400);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
