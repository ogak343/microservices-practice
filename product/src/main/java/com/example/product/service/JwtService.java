package com.example.product.service;

public interface JwtService {
    Long extractId(String token);

    String extractSubject(String token);
}
