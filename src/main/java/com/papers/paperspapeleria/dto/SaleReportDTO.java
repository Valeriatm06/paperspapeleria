package com.papers.paperspapeleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SaleReportDTO {
    private Long id;
    private LocalDateTime date;
    private String clientName;
    private String clientIdentification;
    private Double totalValue;
    private Double taxes;
    private Double discounts;
}