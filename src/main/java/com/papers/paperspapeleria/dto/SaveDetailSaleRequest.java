package com.papers.paperspapeleria.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class SaveDetailSaleRequest {
    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productId;
    
    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;
    
    @NotNull(message = "El precio unitario no puede ser nulo")
    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private Double unitPrice;
    
    @NotNull(message = "El subtotal no puede ser nulo")
    @Min(value = 0, message = "El subtotal no puede ser negativo")
    private Double subtotal;
}