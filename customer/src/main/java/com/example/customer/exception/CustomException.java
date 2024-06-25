package com.example.customer.exception;

import com.example.customer.contants.ErrorMessage;

public class CustomException extends RuntimeException {

    public CustomException(ErrorMessage errorMessage) {
        super(errorMessage.name());
    }
}
