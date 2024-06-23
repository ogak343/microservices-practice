package com.example.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigInteger;

@Data
public class ProductCreateReq {

    @NotEmpty
    private String name;
    private String description;
    @NotNull
    @Min(0)
    private BigInteger price;
    @NotNull
    @Min(0)
    private Integer quantity;
    @NotNull
    @Min(0)
    private Long categoryId;
}
