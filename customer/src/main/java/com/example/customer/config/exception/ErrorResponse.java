package com.example.customer.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse<T> {

    private int code;
    private String description;
    private T body;
}
