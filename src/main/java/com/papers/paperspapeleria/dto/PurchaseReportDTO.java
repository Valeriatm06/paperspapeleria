package com.papers.paperspapeleria.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseReportDTO {
    private Long id;
    private LocalDateTime date; // 👈 CAMBIO: Usar LocalDateTime
    private Double totalValue; // 👈 CAMBIO: Coincide con tu entidad
    private String supplierName; // Nombre del proveedor
    private String supplierIdentification;
    
}