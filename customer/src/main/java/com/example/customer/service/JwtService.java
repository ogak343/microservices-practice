package com.example.customer.service;

import com.example.customer.contants.ClientType;

public interface JwtService {
    Long extractId(String token);

    String extractSubject(String token);

    String generateToken(Long id, ClientType type);
}
