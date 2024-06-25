package com.example.order.client.dto;

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
