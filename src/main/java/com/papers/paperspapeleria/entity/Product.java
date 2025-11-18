package com.papers.paperspapeleria.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private String name;
    
    @Column(name = "precio_compra")
    private Double purchasePrice;
    
    @Column(name = "precio_venta", nullable = false)
    private Double salePrice;

    @Column(name = "stock_actual")
    private Integer actualStock;
    
    @Lob
    private String description;
    
    private String brand;
    private String category;
    
    private String image;
}