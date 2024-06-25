package com.example.product.service;

import com.example.product.contants.ClientType;

public interface JwtService {
    Long extractId(String token);

    String extractSubject(String token);

    String generateToken(Long customerId, ClientType clientType);
}
