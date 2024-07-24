package com.example.order.external.client;

import com.example.order.config.OrderServiceConfig;
import com.example.order.config.exception.CustomException;
import com.example.order.contants.ErrorCode;
import com.example.order.dto.req.OrderCreate;
import com.example.order.external.dto.OrderedProductResp;
import com.example.order.utils.ParserUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductServiceClient {

    private final WebClient webClient;
    private final OrderServiceConfig config;

    public ProductServiceClient(@Qualifier(value = "product") WebClient webClient,
                                OrderServiceConfig config) {
        this.webClient = webClient;
        this.config = config;
    }

    public OrderedProductResp createOrder(OrderCreate dto) {

        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.setBearerAuth(getToken()))
                .bodyValue(dto)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
                    throw new CustomException(ErrorCode.PRODUCT_ORDER_FAILED);
                })
                .bodyToMono(OrderedProductResp.class)
                .block();
    }

    private String getToken() {
        return webClient
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
