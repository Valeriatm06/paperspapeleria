package com.papers.paperspapeleria.service;

import java.util.List;

import com.papers.paperspapeleria.dto.SaleDTO;

public interface SaleService {

    SaleDTO createSale(SaleDTO saleDTO);

    List<SaleDTO> getAllSales();

    void deleteSale(Long id);

    SaleDTO upDateSale(Long id, SaleDTO saleDTO);




}
