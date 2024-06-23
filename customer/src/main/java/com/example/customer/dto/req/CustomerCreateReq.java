package com.example.customer.dto.req;

import lombok.Data;

@Data
public class CustomerCreateReq {

    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String password;
}
