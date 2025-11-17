package com.papers.paperspapeleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SaleInvoiceDetailDTO {

    private Long saleId; // ID de la venta/factura
    private LocalDateTime saleDate; // Fecha de la venta
    private String clientName; // Nombre del cliente
    
    private String productReference; // Ref del producto
    private String productName; // Nombre del producto
    private Integer quantity; // Cantidad vendida
    private Double unitPrice; // Precio unitario
    private Double subtotal; // Subtotal del item
}