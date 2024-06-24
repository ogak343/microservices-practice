package com.example.customer.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerConfirmReq {

    @NotNull
    private Long otpId;
    @NotNull
    private Integer code;
}
