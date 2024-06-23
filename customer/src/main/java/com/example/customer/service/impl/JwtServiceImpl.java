package com.example.customer.service.impl;

import com.example.customer.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {



    @Override
    public Long extractCustomerId(String token) {
        return Long.valueOf(extractClaims(token, Claims::getSubject));
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

    private byte[] getSecretKey() {
        return new byte[16];
    }
}
