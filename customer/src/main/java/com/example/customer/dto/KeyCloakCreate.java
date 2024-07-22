package com.example.customer.dto;

public record KeyCloakCreate(
        String username,
        String email,
        Credentials credentials
) {

    public KeyCloakCreate(String username, String value) {
        this(username, username, new Credentials(false, "PASSWORD", value));
    }

    private record Credentials(
            boolean temporary,
            String type,
            String value
    ) {

    }
}
