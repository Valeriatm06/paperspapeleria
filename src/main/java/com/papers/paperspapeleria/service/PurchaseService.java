package com.papers.paperspapeleria.service;

import java.util.List;

import com.papers.paperspapeleria.dto.PurchaseDTO;
import com.papers.paperspapeleria.dto.SavePurchaseRequest;

public interface PurchaseService {
    
    PurchaseDTO createPurchase(PurchaseDTO purchaseDTO);

    List<PurchaseDTO> getAllPurchases();
    
    PurchaseDTO getPurchaseById(Long id);
    
    PurchaseDTO updatePurchase(Long id, PurchaseDTO purchaseDTO);
    
    void deletePurchase(Long id);

    PurchaseDTO savePurchase(SavePurchaseRequest request);
}