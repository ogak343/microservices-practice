package com.example.order.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductResp(
        Long id,
        String name,
        String description,
        BigInteger price,
        int quantity
) {

}
