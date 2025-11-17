package com.papers.paperspapeleria.dto;

import lombok.Data;
import java.util.List;

@Data
public class SavePurchaseRequest {
    // Corresponde a 'selectedSupplier' del frontend (identificacion del User)
    private String supplierId; 
    
    // Corresponde a 'purchaseDate' del frontend (ISO String)
    private String date;       
    
    private Double taxes;
    private Double discounts;
    private Double totalValue;
    
    // Array de DetailPurchaseDTO
    private List<DetailPurchaseDTO> details; 
}