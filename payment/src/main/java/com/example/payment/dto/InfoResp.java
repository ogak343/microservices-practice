package com.example.payment.dto;

import lombok.Data;

@Data
public class InfoResp<T> {

    private int code;
    private String message;
    private T data;

    public InfoResp(T data) {
        this.data = data;
    }
}
