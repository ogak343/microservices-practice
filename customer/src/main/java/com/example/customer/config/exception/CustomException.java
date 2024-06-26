package com.example.customer.config.exception;

import com.example.customer.contants.ErrorMessage;

public class CustomException extends RuntimeException {

    public CustomException(ErrorMessage errorMessage) {
        super(errorMessage.name());
    }

    public CustomException(String string) {
        super(string);
    }
}
