package com.example.order.dto.req;

import lombok.Data;

import java.util.Set;

@Data
public class OrderUpdate {
    private Long orderId;
    private Set<ProductDetailsReq> productDetails;
}
