package com.example.payment.entity;

import com.example.payment.constants.PaymentStatus;
import com.example.payment.external.feign.dto.OrderResp;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Setter
@Getter
public class Payment {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private BigInteger amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Column(nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
    private OffsetDateTime performedAt;
    private OffsetDateTime cancelledAt;

    public Payment(OrderResp order) {
        this.orderId = order.getId();
        this.amount = order.getTotalPrice();
        this.paymentStatus = PaymentStatus.CREATED;
    }

    public Payment() {
    }
}
