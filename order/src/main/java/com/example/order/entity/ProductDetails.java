package com.example.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_details")
@Setter
@Getter
public class ProductDetails {
    @Id
    private Long id;
    @ManyToOne
    @Column(nullable = false)
    private Order order;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private int quantity;
}
