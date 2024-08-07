package com.example.order.contants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_AMOUNT_SPECIFIED(400),
    INVALID_TOKEN(401),
    ORDER_NOT_FOUND(404),
    PRODUCT_DETAIL_NOT_FOUND(404),
    INVALID_TOPIC(500),
    PRODUCT_ORDER_FAILED(500);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
