package com.example.payment.external;

import com.example.payment.config.PaymentServiceConfig;
import com.example.payment.external.dto.OrderResp;
import com.example.payment.utils.ParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class OrderService {

    private final WebClient keycloakWebClient;
    private final WebClient orderWebClient;
    private final PaymentServiceConfig config;

    public OrderService(@Qualifier(value = "keycloak") WebClient keycloakWebClient,
                        @Qualifier(value = "order") WebClient orderWebClient,
                        PaymentServiceConfig config) {
        this.keycloakWebClient = keycloakWebClient;
        this.orderWebClient = orderWebClient;
        this.config = config;
    }

    public OrderResp getOrder(Long orderId) {
        return orderWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/orders/" + orderId).build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .retrieve()
                .bodyToMono(OrderResp.class)
                .doOnError(WebClientResponseException.class, ex -> log.error("Error response: {}", ex.getMessage(), ex)
                ).block();
    }

    public void verifyOrder(Long orderId) {
        orderWebClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/orders/verify")
                        .queryParam("orderId", orderId)
                        .build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnError(WebClientResponseException.class, ex -> log.error("Error response: {}", ex.getMessage(), ex))
                .block();
    }

    private String getToken() {
        return keycloakWebClient
                .post()
                .uri(config.getTokenPath())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters
                        .fromFormData("client_id", config.getClientId())
                        .with("client_secret", config.getClientSecret())
                        .with("grant_type", config.getGrantType()))
                .retrieve()
                .bodyToMono(String.class)
                .map(resp -> ParserUtil.parseToString(resp, "access_token")).block();

    }
}
