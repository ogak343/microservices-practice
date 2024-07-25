package com.example.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${web.clients.product.url}")
    private String productServiceUrl;

    @Value("${spring.security.oauth2.keycloak-url}")
    private String keycloakUrl;

    @Bean(name = "product")
    public WebClient productClient() {

        return WebClient
                .builder()
                .baseUrl(productServiceUrl)
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
