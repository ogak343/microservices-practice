package com.example.product.dto.request;

import java.util.Map;

public record OrderCreate(Map<Long, Integer> products) {
}
