package com.example.customer.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CustomerCreateReq {
    private String firstname;
    private String lastname;
    @NotEmpty
    private String email;
    private String address;
    @NotEmpty
    private String password;
}
