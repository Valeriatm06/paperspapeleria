package com.papers.paperspapeleria.dto;

import lombok.Data;

@Data
public class ProductDetailDTO {
    private Long id;
    private String name;
    private String reference;
    private Double purchasePrice; // Usamos Double para dinero
    private Double salePrice;
    private Integer actualStock; // Usamos Integer para stock
    private String description;
    private String brand;
    private String category;
}
