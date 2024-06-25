package com.example.product.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class OrderCreate {
    private Map<Long, Integer> products;
}
