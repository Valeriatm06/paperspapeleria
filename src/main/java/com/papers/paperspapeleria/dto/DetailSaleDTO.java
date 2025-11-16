package com.papers.paperspapeleria.dto;

import lombok.Data;

@Data
public class DetailSaleDTO {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}