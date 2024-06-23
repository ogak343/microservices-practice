package com.example.customer.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerUpdateReq extends CustomerCreateReq {

    private Long id;
}
