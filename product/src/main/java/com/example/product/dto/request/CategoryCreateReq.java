package com.example.product.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryCreateReq {
    @NotEmpty
    private String name;
    private String description;

}
