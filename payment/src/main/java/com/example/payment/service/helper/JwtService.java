package com.example.payment.service.helper;

import com.example.payment.constants.ClientType;

public interface JwtService {
    String generateToken(Long principal, ClientType clientType);

    Long extractId(String token);

    String extractSubject(String token);
}
