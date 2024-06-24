package com.example.customer.service;

import com.example.customer.contants.ClientType;
import com.example.customer.dto.resp.LoginResp;

public interface JwtService {
    Long extractId(String token);

    String extractSubject(String token);

    LoginResp generateToken(Long id, ClientType type);
}
