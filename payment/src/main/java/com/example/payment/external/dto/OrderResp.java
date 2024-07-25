package com.example.payment.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResp {

    private Long id;
    private BigInteger totalPrice;
    private Set<ProductResp> productDetails;
    private String customerId;
    private Status status;
    private OffsetDateTime createdAt;
    private OffsetDateTime payedAt;
}
