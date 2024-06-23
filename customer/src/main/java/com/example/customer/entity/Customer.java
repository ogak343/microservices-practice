package com.example.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Setter
@Getter
public class Customer {

    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    private boolean active;

}
