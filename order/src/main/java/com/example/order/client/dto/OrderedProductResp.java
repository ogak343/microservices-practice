package com.example.order.client.dto;

import com.example.order.dto.resp.ProductResp;

import lombok.Data;

import java.math.BigInteger;
import java.util.Set;

@Data
public class OrderedProductResp {
    private BigInteger totalPrice;
    private Set<ProductResp> products;
}
