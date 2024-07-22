package com.example.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "customer")
@Setter
@Getter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    @Column(name = "keycloak_user_id")
    private String keycloakUserId;
    private boolean active;
    private Timestamp deletedAt;
}
