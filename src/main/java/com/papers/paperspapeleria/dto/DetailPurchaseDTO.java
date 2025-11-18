package com.papers.paperspapeleria.dto;

import lombok.Data;

@Data
public class DetailPurchaseDTO {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}