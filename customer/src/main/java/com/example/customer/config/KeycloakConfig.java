package com.example.customer.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String authServerUrl;

    @Value("${spring.security.oauth2.keycloak-realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Bean
    public KeycloakBuilder keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret);
    }

    @Bean
    public RealmResource realmResource(KeycloakBuilder keycloak) {
        return keycloak.build().realm(realm);
    }

}
