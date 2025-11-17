package com.papers.paperspapeleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseInvoiceDetailDTO {

    private Long purchaseId; // ID de la factura/compra
    private LocalDateTime purchaseDate; // Fecha de la compra
    private String supplierName; // Nombre del proveedor
    
    private String productReference; // Ref del producto
    private String productName; // Nombre del producto
    private Integer quantity; // Cantidad comprada
    private Double unitPrice; // Precio unitario
    private Double subtotal; // Subtotal del item
}