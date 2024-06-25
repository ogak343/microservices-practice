package com.example.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_details")
@Setter
@Getter
@NoArgsConstructor
public class ProductDetails {
    @Id
    private Long id;
    @ManyToOne
    private Order order;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private int quantity;

    public ProductDetails(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
