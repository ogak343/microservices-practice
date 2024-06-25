package com.example.order.entity;

import com.example.order.contants.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "order")
@Setter
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    @JoinTable(name = "product_details", joinColumns = @JoinColumn(name = "order_id"))
    private Set<ProductDetails> productDetails;
    @Column(nullable = false)
    private BigInteger totalPrice;
    @Column(nullable = false)
    private Long customerId;
    @Column(nullable = false)
    private Status status;
    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    private OffsetDateTime lastModifiedAt;
    private OffsetDateTime payedAt;

    public Order(BigInteger totalPrice, Long customerId) {
        this.totalPrice = totalPrice;
        this.customerId = customerId;
    }
}
