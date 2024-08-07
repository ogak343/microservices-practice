package com.example.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Table(name = "product_details")
@Setter
@Getter
@NoArgsConstructor
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String name;
    private String description;
    private BigInteger price;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private int quantity;
}
