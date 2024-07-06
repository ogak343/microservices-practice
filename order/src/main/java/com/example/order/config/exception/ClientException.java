package com.example.order.config.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientException extends RuntimeException {

    private int code;
    private String message;

    public ClientException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
