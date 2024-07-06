package com.example.customer.contants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_OTP(400),
    WRONG_OTP_CODE(401),
    INVALID_TOKEN(401),
    OTP_EXPIRED(401),
    WRONG_PASSWORD(403),
    WRONG_CREDENTIALS(403),
    CUSTOMER_NOT_FOUND(404);

    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

}
