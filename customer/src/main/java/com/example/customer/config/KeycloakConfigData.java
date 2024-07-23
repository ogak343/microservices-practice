package com.example.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class KeycloakConfigData {

    @Value("${spring.security.oauth2.keycloak-url}")
    private String url;
    @Value("${spring.security.oauth2.keycloak-admin}")
    private String adminPath;
    @Value("${spring.security.oauth2.keycloak-token}")
    private String tokenPath;
    @Value("${spring.security.oauth2.keycloak-realm}")
    private String realm;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.keycloak.authorization-grant-type}")
    private String grantType;

}
