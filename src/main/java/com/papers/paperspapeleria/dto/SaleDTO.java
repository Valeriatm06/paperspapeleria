package com.papers.paperspapeleria.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDTO {
    private Long id;

    private String userId;
    private String clienteName;
    private LocalDateTime date;
    private Double taxes;
    private Double discounts;
    private Double totalValue;

    private List<DetailSaleDTO> details;
}