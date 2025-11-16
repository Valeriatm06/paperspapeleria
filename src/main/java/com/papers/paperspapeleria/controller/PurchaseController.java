package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.dto.PurchaseDTO;
import com.papers.paperspapeleria.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compras")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseDTO createPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.createPurchase(purchaseDTO);
    }

}
