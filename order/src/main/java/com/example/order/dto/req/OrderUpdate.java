package com.example.order.dto.req;

import lombok.Data;

import java.util.Map;

@Data
public class OrderUpdate {
    private Long orderId;
    private Map<Long, Integer> orders;
}
