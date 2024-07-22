package com.example.customer.external;

import com.example.customer.config.KeycloakConfigData;
import com.example.customer.dto.KeyCloakCreate;
import com.example.customer.utils.ParserUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KeyCloakService {

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
                .uri(configData.getAuthServerUrl())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .body(keycloakCreate, KeyCloakCreate.class)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response -> {
                    throw new RuntimeException();
                });
    }

    public void enableUser(String userId) {

        keycloakClient
                .put()
                .uri(uriBuilder -> uriBuilder.path("/" + userId).build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .body(new KeyCloakUpdate(true), KeyCloakUpdate.class)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response -> {
                    throw new RuntimeException("Failed to enable user");
                });

    }

    public String searchByUsername(String email) {
        var respDto = keycloakClient
                .get()
                .uri(url -> url.path("")
                        .queryParam("username", email)
                        .build()
                )
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .retrieve()
                .bodyToMono(KeyCloakUserDto.class).block();

        if (respDto == null) throw new RuntimeException("Null response");

        return respDto.id;
    }

    private String getToken() {
        return keycloakClient
                .post()
                .uri(configData.getAuthServerUrl())
                .body(new KeyCloakLogin(configData.getGrantType(), configData.getClientId(), configData.getClientSecret()),
                        KeyCloakLogin.class)
                .retrieve()
                .bodyToMono(String.class)
                .map(resp-> ParserUtil.parseToString(resp, "access_token")).block();

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record KeyCloakUserDto(
            String id
    ) {
    }

    private record KeyCloakLogin(
            @JsonProperty("grant_type")
            String grantType,
            @JsonProperty("client_id")
            String clientId,
            @JsonProperty("client_secret")
            String clientSecret
    ) {
    }

    private record KeyCloakUpdate(
            boolean enabled
    ) {
    }
}
