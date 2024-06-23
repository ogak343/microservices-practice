package com.example.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductUpdateReq extends ProductCreateReq {

    @NotNull
    private Long id;
}
