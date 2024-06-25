package com.example.order.config;

import com.example.order.contants.ClientType;
import com.example.order.service.JwtService;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@EnableFeignClients
@Configuration
public class FeignClientConfig {

    private final JwtService jwtService;

    @Autowired
    public FeignClientConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public Decoder feignDecoder() {
        return new FeignClientDecoder();
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {

        var customerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return requestTemp -> requestTemp.header(AUTHORIZATION,
                "Bearer " + jwtService.generateToken(customerId, ClientType.SERVICE)
        );
    }
}
