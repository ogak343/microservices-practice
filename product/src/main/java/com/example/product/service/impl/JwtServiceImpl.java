package com.example.product.service.impl;

import com.example.product.contants.ClientType;
import com.example.product.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public Long extractId(String token) {
        return Long.valueOf(extractClaims(token, Claims::getId));
    }

    @Override
    public String extractSubject(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public String generateToken(Long customerId, ClientType clientType) {
        return "";
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(
                Jwts.parserBuilder()
                        .setSigningKey(getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
        );
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}

