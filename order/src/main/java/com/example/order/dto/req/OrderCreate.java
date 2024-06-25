package com.example.order.dto.req;

import lombok.Data;

import java.util.Set;

@Data
public class OrderCreate {
    private Set<ProductDetailsReq> products;
}
