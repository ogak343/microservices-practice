package com.example.customer.contants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WRONG_OTP_CODE(400),
    WRONG_PASSWORD(400),
    INVALID_TOKEN(400),
    WRONG_CREDENTIALS(400),
    OTP_EXPIRED(400);

    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

}
