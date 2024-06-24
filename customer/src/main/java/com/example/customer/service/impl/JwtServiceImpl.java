package com.example.customer.service.impl;

import com.example.customer.contants.ClientType;
import com.example.customer.dto.resp.LoginResp;
import com.example.customer.service.JwtService;
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

    @Value("${jwt.expiresIn}")
    private Long expiresIn;

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
    public LoginResp generateToken(Long id, ClientType type) {
        var token = Jwts
                .builder()
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .setId(String.valueOf(id))
                .setSubject(type.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiresIn))
                .compact();

        return new LoginResp(200, "Success", token);
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
