package com.example.customer.service;

public interface JwtService {
    Long extractCustomerId(String token);
}
