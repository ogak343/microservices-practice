package com.example.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

@Entity
@Table(name = "otp_entity")
@Setter
@Getter
public class PaymentOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer code;
    @ManyToOne
    private Payment payment;
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    @Column(nullable = false)
    private OffsetDateTime expiredAt;
    private OffsetDateTime confirmedAt;

    @SneakyThrows
    public PaymentOtp(Payment payment) {
        this.payment = payment;
        this.code = SecureRandom.getInstance("SHA1PRNG").nextInt(100000, 999999);
        this.createdAt = OffsetDateTime.now();
        this.expiredAt = OffsetDateTime.now().plusMinutes(5);
    }

    public PaymentOtp() {
    }
}
