package com.example.customer.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerUpdateReq extends CustomerCreateReq {

    @NotNull
    private Long id;
}
