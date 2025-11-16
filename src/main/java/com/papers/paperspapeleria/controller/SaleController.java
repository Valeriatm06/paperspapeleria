package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.dto.SaleDTO;
import com.papers.paperspapeleria.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDTO createSale(@RequestBody SaleDTO saleDTO) {
        
        return saleService.createSale(saleDTO);
    }
}