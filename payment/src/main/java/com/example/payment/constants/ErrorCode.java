package com.example.payment.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_STATUS(400),
    INVALID_OTP(400),
    INVALID_TOKEN(401),
    CUSTOMER_NOT_FOUND_IN_CACHE(404),
    SERVER_SIDE_ERROR(500);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
