package com.example.payment.service;

import com.example.payment.constants.ClientType;

public interface JwtService {
    String generateToken(Long principal, ClientType clientType);
}
