package com.example.order.config;

import feign.codec.Decoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@Configuration
public class FeignClientConfig {

    @Bean
    public Decoder feignDecoder() {
        return new FeignClientDecoder();
    }
}
