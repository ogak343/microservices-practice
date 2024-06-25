package com.example.order.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class OrderCreate {
    @NotNull
    private Map<Long, Integer> products;
}
