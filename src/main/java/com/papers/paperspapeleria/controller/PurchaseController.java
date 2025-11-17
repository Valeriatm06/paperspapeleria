package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.dto.PurchaseDTO;
import com.papers.paperspapeleria.dto.SavePurchaseRequest;
import com.papers.paperspapeleria.service.PurchaseService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    //@PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    //public PurchaseDTO createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
      //  return purchaseService.createPurchase(purchaseDTO);
    //}
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseDTO getPurchaseById(@PathVariable Long id) {
        return purchaseService.getPurchaseById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseDTO updatePurchase(@PathVariable Long id, @RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.updatePurchase(id, purchaseDTO);
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> savePurchase(@Valid @RequestBody SavePurchaseRequest request) {
        try {
            PurchaseDTO savedPurchase = purchaseService.savePurchase(request);
            return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Error al guardar compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
