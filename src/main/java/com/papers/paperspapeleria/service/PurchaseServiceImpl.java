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

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public List<PurchaseDTO> getAllPurchases() {
		List<Purchase> purchases = purchaseRepository.findAll();
        return purchases.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
	}

	@Override
	public PurchaseDTO getPurchaseById(Long id) {
		Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + id));
        return convertToDTO(purchase);
	}

	@Override
    @Transactional
	public PurchaseDTO updatePurchase(Long id, PurchaseDTO purchaseDTO) {
		Purchase actualPurchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + id));

        if (purchaseDTO.getUserId() != null &&
            !purchaseDTO.getUserId().equals(actualPurchase.getSupplier().getIdentification())) {
            
            User newSupplier = userRepository.findById(purchaseDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor (Tercero) no encontrado con ID: " + purchaseDTO.getUserId()));
            
            actualPurchase.setSupplier(newSupplier);
        }
        
        if (purchaseDTO.getTotalValue() != null) {
            actualPurchase.setTotalValue(purchaseDTO.getTotalValue());
        }
        if (purchaseDTO.getTaxes() != null) {
            actualPurchase.setTaxes(purchaseDTO.getTaxes());
        }
        if (purchaseDTO.getDiscounts() != null) {
            actualPurchase.setDiscounts(purchaseDTO.getDiscounts());
        }

        Purchase savedPurchase = purchaseRepository.save(actualPurchase);
        return convertToDTO(savedPurchase);
	}

	@Override
    @Transactional
	public void deletePurchase(Long id) {
		Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + id));
        for (DetailPurchase detail : purchase.getDetails()) {
            Product product = detail.getProduct();
            int newStock = product.getActualStock() - detail.getQuantity();

            if (newStock < 0) {
            }
            
            product.setActualStock(newStock);
        }
        
        purchaseRepository.delete(purchase);
	}
}