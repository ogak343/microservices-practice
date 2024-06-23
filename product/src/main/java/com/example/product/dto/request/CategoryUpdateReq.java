package com.example.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryUpdateReq extends CategoryCreateReq {
    @NotNull
    private Long id;
}
