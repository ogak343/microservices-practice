package com.example.customer.dto.resp;

import lombok.Data;

@Data
public class CustomerResp {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private boolean active;
}
