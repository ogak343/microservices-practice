package com.example.payment.external.feign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResp {

    private Long id;
    private BigInteger totalPrice;
    private Long customerId;
    private Status status;
    private OffsetDateTime createdAt;
    private OffsetDateTime payedAt;
}
