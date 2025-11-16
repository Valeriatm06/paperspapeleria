package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.DetailPurchaseDTO;
import com.papers.paperspapeleria.dto.PurchaseDTO;
import com.papers.paperspapeleria.entity.User; // Importa tu entidad User
import com.papers.paperspapeleria.entity.DetailPurchase; // Importa tu entidad DetailPurchase
import com.papers.paperspapeleria.entity.Product; // Importa tu entidad Product
import com.papers.paperspapeleria.entity.Purchase; // Importa tu entidad Purchase
import com.papers.paperspapeleria.repository.ProductRepository;
import com.papers.paperspapeleria.repository.PurchaseRepository;
import com.papers.paperspapeleria.repository.UserRepository; // Importa tu UserRepository

import jakarta.transaction.Transactional; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, 
                               ProductRepository productRepository, 
                               UserRepository userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) {
        
        User supplier = userRepository.findById(purchaseDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + purchaseDTO.getUserId()));

        // 2. Mapear y crear la entidad Purchase (Maestro)
        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setDate(LocalDateTime.now());
        purchase.setTaxes(purchaseDTO.getTaxes());
        purchase.setDiscounts(purchaseDTO.getDiscounts());
        purchase.setTotalValue(purchaseDTO.getTotalValue());

        List<DetailPurchase> detailPurchases = new ArrayList<>();
        
        for (DetailPurchaseDTO detailDTO : purchaseDTO.getDetails()) {
            
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detailDTO.getProductId()));

            DetailPurchase detail = new DetailPurchase();
            detail.setPurchase(purchase);
            detail.setProduct(product);
            detail.setQuantity(detailDTO.getQuantity());
            detail.setUnitPrice(detailDTO.getUnitPrice());
            detail.setSubtotal(detailDTO.getSubtotal());
            
            detail.setInitialStock(product.getActualStock());
            product.setActualStock(product.getActualStock() + detailDTO.getQuantity());
            
            detailPurchases.add(detail);
        }

        purchase.setDetails(detailPurchases);
        Purchase savedPurchase = purchaseRepository.save(purchase);

        return convertToDTO(savedPurchase);
    }
    

    private PurchaseDTO convertToDTO(Purchase purchase) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(purchase.getId());
        dto.setUserId(purchase.getSupplier().getIdentification());
        dto.setDate(purchase.getDate());
        dto.setTaxes(purchase.getTaxes());
        dto.setDiscounts(purchase.getDiscounts());
        dto.setTotalValue(purchase.getTotalValue());
        dto.setDetails(purchase.getDetails().stream()
                .map(this::convertDetailToDTO)
                .toList());
        return dto;
    }

    private DetailPurchaseDTO convertDetailToDTO(DetailPurchase detail) {
        DetailPurchaseDTO dto = new DetailPurchaseDTO();
        dto.setProductId(detail.getProduct().getId());
        dto.setQuantity(detail.getQuantity());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setSubtotal(detail.getSubtotal());
        return dto;
    }
}