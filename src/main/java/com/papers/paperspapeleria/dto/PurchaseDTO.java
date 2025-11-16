package com.papers.paperspapeleria.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseDTO {
    private Long id;
    private String userId;
    private String provName;
    private LocalDateTime date;
    private Double taxes;
    private Double discounts;
    private Double totalValue;
    
    private List<DetailPurchaseDTO> details;
}