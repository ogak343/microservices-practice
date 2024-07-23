package com.example.customer.dto.req;

import jakarta.validation.constraints.NotEmpty;

public record LoginReq(
        @NotEmpty String email,
        @NotEmpty String password
) {
}
