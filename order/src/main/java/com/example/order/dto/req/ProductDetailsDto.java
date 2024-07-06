package com.example.order.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailsDto {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;

}
