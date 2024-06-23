package com.example.product.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;

import java.math.BigInteger;

@Entity
@Table(name = "product")
@Setter
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigInteger price;
    private int quantity;
    private boolean available;
    @ManyToOne
    private Category category;
}
