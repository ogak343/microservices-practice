package com.example.payment.service.impl;

import com.example.payment.constants.ClientType;
import com.example.payment.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiresIn}")
    private Long expiresIn;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public String generateToken(Long id, ClientType type) {
        return Jwts
                .builder()
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .setId(String.valueOf(id))
                .setSubject(type.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiresIn))
                .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}