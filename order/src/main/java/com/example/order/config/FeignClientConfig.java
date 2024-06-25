package com.example.order.config;

import com.example.order.contants.ClientType;
import com.example.order.service.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientConfig implements RequestInterceptor {

    private final JwtService jwtService;

    @Autowired
    public FeignClientConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public Decoder feignDecoder() {
        return new FeignClientDecoder();
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
