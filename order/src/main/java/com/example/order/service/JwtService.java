package com.example.order.service;

import com.example.order.contants.ClientType;

public interface JwtService {
    Long extractId(String token);

    String extractSubject(String token);

    String generateToken(Long id, ClientType type);
}
