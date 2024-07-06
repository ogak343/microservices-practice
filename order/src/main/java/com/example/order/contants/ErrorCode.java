package com.example.order.contants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_TOKEN(401);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
