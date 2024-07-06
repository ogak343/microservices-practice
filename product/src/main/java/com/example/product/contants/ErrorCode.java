package com.example.product.contants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_AMOUNT(400),
    NOT_ALL_PRODUCTS_FOUND(400),
    INSUFFICIENT_QUANTITY(400),
    INVALID_TOKEN(401),
    CATEGORY_NOT_FOUND(404),
    PRODUCT_NOT_FOUND(404),
    CATEGORY_EXISTS(600);

    ErrorCode(int code) {
        this.code = code;
    }

    private final int code;
}
