package com.example.customer.dto.resp;

public record LoginResp(
    int code,
    String message,
    String accessToken
) {
}
