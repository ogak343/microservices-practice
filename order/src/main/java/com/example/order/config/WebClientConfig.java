package com.example.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${web.clients.product.url}")
    private String productServiceUrl;

    @Bean(name = "product")
    public WebClient webClient() {

        return WebClient
                .builder()
                .baseUrl(productServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    private String getToken() {
        return "seg";
    }
}
