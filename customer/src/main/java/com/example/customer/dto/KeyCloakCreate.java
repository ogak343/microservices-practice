package com.example.customer.dto;

import java.util.List;

public record KeyCloakCreate(
        String username,
        String email,
        List<Credentials> credentials
) {

    public KeyCloakCreate(String username, String value) {
        this(username, username, List.of(new Credentials(false, "PASSWORD", value)));
    }

    private record Credentials(
            boolean temporary,
            String type,
            String value
    ) {

    }
}
