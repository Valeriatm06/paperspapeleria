package com.papers.paperspapeleria.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "detail_purchase")
public class DetailPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;
    
    @Column(name = "precio_unitario", nullable = false)
    private Double unitPrice;
    
    @Column(name = "valor_subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "stock_inicial")
    private Integer initialStock;
}