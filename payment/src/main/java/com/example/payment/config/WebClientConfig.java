package com.example.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${web.clients.order.url}")
    private String orderServiceUrl;

    @Value("${spring.security.oauth2.keycloak-url}")
    private String keycloakUrl;

    @Bean(name = "order")
    public WebClient productClient() {

        return WebClient
                .builder()
                .baseUrl(orderServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean(name = "keycloak")
    public WebClient keycloakClient() {
        return WebClient
                .builder()
                .baseUrl(keycloakUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}