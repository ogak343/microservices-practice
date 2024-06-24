package com.example.customer.dto.resp;

import lombok.Data;

@Data
public class LoginResp {

    private int status;
    private String message;
    private String token;

    public LoginResp(int status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }
}
