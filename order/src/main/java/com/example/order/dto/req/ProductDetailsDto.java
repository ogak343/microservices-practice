package com.example.order.dto.req;

import com.example.order.contants.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailsDto {
    @NotNull
    private Long detailId;
    @NotNull
    private Operation operation;
    @NotNull
    private Integer quantity;

}
