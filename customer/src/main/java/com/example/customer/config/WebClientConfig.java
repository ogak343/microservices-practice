package com.example.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final KeycloakConfigData keycloakConfigData;

    public WebClientConfig(KeycloakConfigData keycloakConfigData) {
        this.keycloakConfigData = keycloakConfigData;
    }

    @Bean(name = "keycloak")
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(keycloakConfigData.getAuthServerUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
