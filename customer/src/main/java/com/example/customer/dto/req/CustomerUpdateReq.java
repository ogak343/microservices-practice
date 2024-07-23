package com.example.customer.dto.req;

public record CustomerUpdateReq(
        String firstname,
        String lastname,
        String address
) {
}
