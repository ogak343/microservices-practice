package com.example.customer.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginReq {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
