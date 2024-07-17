package com.example.payment.dto;

public record InfoResp<T>(
        int code,
        String message,
        T data
) {

    public InfoResp(T data) {
        this(200, "OK", data);
    }
}
