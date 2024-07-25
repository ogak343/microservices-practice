package com.example.order.external;

import com.example.order.config.OrderServiceConfig;
import com.example.order.dto.req.OrderCreate;
import com.example.order.external.dto.OrderedProductResp;
import com.example.order.utils.ParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class ProductService {


    private final WebClient keycloakWebClient;
    private final WebClient productWebClient;
    private final OrderServiceConfig config;

    public ProductService(@Qualifier(value = "keycloak") WebClient keycloakWebClient,
                          @Qualifier(value = "product") WebClient productWebClient,
                          OrderServiceConfig config) {
        this.keycloakWebClient = keycloakWebClient;
        this.productWebClient = productWebClient;
        this.config = config;
    }

    public OrderedProductResp createOrder(OrderCreate dto) {

        return productWebClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/products/order/create").build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(OrderedProductResp.class)
                .doOnError(WebClientResponseException.class, ex -> log.error("Error response: {}", ex.getMessage(), ex)
                ).block();
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
