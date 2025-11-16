package com.papers.paperspapeleria.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.papers.paperspapeleria.entity.User;

@Entity
@Data
@Table(name = "compas") 
public class Purchase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User supplier;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime date;

    @Column(name = "valor_impuestos", nullable = false)
    private Double taxes;

    @Column(name = "valor_descuentos", nullable = false)
    private Double Discounts;

    @Column(name = "valor_total", nullable = false)
    private Double totalValue;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailPurchase> details;
}