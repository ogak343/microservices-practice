package com.example.payment.exception;

import com.example.payment.constants.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private int code;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.name());
        this.code = errorCode.getCode();
    }
}
