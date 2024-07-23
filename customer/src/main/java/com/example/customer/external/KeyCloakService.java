package com.example.customer.external;

import com.example.customer.config.KeycloakConfigData;
import com.example.customer.config.exception.CustomException;
import com.example.customer.contants.ErrorCode;
import com.example.customer.dto.KeyCloakCreate;
import com.example.customer.dto.req.LoginReq;
import com.example.customer.utils.ParserUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class KeyCloakService {

    private static final Logger log = LoggerFactory.getLogger(KeyCloakService.class);
    private final KeycloakConfigData configData;
    private final WebClient keycloakClient;

    @Autowired
    public KeyCloakService(KeycloakConfigData configData,
                           @Qualifier(value = "keycloak") WebClient keycloakClient) {
        this.configData = configData;
        this.keycloakClient = keycloakClient;
    }

    public void createUser(KeyCloakCreate keycloakCreate) {

        keycloakClient
                .post()
                .uri(configData.getAdminPath() + "/users")
                .header("Content-Type", "application/json")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .bodyValue(keycloakCreate)
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnError(WebClientResponseException.class, ex -> log.error("Error response: {}", ex.getMessage(), ex))
                .block();
    }

    public void enableUser(String userId) {

        keycloakClient
                .put()
                .uri(uriBuilder -> uriBuilder.path(configData.getAdminPath() + "/users/" + userId).build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .bodyValue(new KeyCloakUpdate(true, true))
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnError(WebClientResponseException.class, ex -> log.error("Error response: {}", ex.getMessage(), ex)
                ).block();

    }

    public String searchByUsername(String email) {
        var respDto = keycloakClient
                .get()
                .uri(url -> url.path(configData.getAdminPath() + "/users")
                        .queryParam("username", email)
                        .build()
                )
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .retrieve()
                .bodyToFlux(KeyCloakUserDto.class)
                .map(KeyCloakUserDto::id)
                .blockFirst();

        if (respDto == null) throw new RuntimeException("Null response");

        return respDto;
    }

    public String login(LoginReq dto) {
        return keycloakClient
                .post()
                .uri(configData.getTokenPath())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters
                        .fromFormData("client_id", configData.getClientId())
                        .with("client_secret", configData.getClientSecret())
                        .with("grant_type", "password")
                        .with("username", dto.email())
                        .with("password", dto.password()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
                })
                .map(resp -> ParserUtil.parseToString(resp, "access_token")).block();
    }

    private String getToken() {

        return keycloakClient
                .post()
                .uri(configData.getTokenPath())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters
                        .fromFormData("client_id", configData.getClientId())
                        .with("client_secret", configData.getClientSecret())
                        .with("grant_type", configData.getGrantType()))
                .retrieve()
                .bodyToMono(String.class)
                .map(resp -> ParserUtil.parseToString(resp, "access_token")).block();

    }

    public void deleteUser(String keycloakUserId) {
        keycloakClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path(configData.getAdminPath() + "/users/" + keycloakUserId).build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnError(WebClientResponseException.class, ex -> log.error("Error response: {}", ex.getMessage(), ex)
                ).block();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KeyCloakUserDto(
            String id
    ) {
    }

    public record KeyCloakUpdate(
            Boolean enabled,
            Boolean emailVerified
    ) {
    }
}
