package com.example.order.entity;

import com.example.order.contants.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order", schema = "public")
@Setter
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ProductDetails> productDetails = new HashSet<>();
    @Column(nullable = false)
    private BigInteger totalPrice;
    @Column(nullable = false)
    private Long customerId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING_FOR_PAYMENT;
    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    private OffsetDateTime lastModifiedAt;
    private OffsetDateTime payedAt;
}
