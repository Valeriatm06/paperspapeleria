package com.papers.paperspapeleria.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "ventas")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User client;

    @Column(name = "valor_impuestos")
    private Double taxes;

    @Column(name = "valor_descuentos")
    private Double discounts;

    @Column(name = "valor_total", nullable = false)
    private Double totalValue;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailSale> detalles = new ArrayList<>();
}