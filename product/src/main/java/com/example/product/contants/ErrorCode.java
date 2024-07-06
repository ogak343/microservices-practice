package com.example.product.contants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_AMOUNT(400),
    INSUFFICIENT_QUANTITY(400),
    INVALID_TOKEN(401),
    CATEGORY_NOT_FOUND(404),
    NOT_ALL_PRODUCTS_FOUND(404),
    PRODUCT_NOT_FOUND(404),
    CATEGORY_EXISTS(409);

    ErrorCode(int code) {
        this.code = code;
    }

    private final int code;
}
