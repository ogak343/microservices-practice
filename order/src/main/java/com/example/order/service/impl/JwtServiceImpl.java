package com.example.order.service.impl;

import com.example.order.contants.ClientType;
import com.example.order.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
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

    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(
                Jwts.parserBuilder()
                        .setSigningKey(getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
        );
    }

    @Override
    public String generateToken(Long id, ClientType type) {
        return Jwts
                .builder()
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .setId(String.valueOf(id))
                .setSubject(type.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 5))
                .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
