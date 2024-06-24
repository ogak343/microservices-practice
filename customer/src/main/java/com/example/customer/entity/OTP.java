package com.example.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer code;
    @ManyToOne
    private Customer customer;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiredAt;

    @SneakyThrows
    public OTP(Customer customer) {
        this.code = SecureRandom.getInstance("SHA1PRNG").nextInt(100000, 999999);
        this.customer = customer;
        this.createdAt = OffsetDateTime.now();
        this.expiredAt = OffsetDateTime.now().plusMinutes(30);
    }
}
