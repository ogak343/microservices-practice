package com.example.order.config.helpers;

import com.example.order.contants.ClientType;
import com.example.order.service.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    private final JwtService jwtService;

    @Autowired
    public FeignClientInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Long) {
            String token = jwtService.generateToken((Long) principal, ClientType.SERVICE);
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
