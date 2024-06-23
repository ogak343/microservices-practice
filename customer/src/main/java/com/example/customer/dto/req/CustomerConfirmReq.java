package com.example.customer.dto.req;

import lombok.Data;

@Data
public class CustomerConfirmReq {

    private Long otpId;
    private Integer code;
}
