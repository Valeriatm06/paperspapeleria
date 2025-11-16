package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.DetailSaleDTO;
import com.papers.paperspapeleria.dto.SaleDTO;
import com.papers.paperspapeleria.entity.*;
import com.papers.paperspapeleria.repository.ProductRepository;
import com.papers.paperspapeleria.repository.SaleRepository;
import com.papers.paperspapeleria.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public SaleDTO createSale(SaleDTO saleDTO) {
        User cliente = userRepository.findById(saleDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente (Tercero) no encontrado con ID: " + saleDTO.getUserId()));

        Sale newSale = new Sale();
        newSale.setClient(cliente);
        newSale.setDate(LocalDateTime.now());
        newSale.setTaxes(saleDTO.getTaxes());
        newSale.setDiscounts(saleDTO.getDiscounts());
        newSale.setTotalValue(saleDTO.getTotalValue());

        for (DetailSaleDTO detailDTO : saleDTO.getDetails()) {
            
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + detailDTO.getProductId()));

            if (product.getActualStock() < detailDTO.getQuantity()) {
                throw new IllegalArgumentException("No hay stock suficiente para el producto: " + product.getName());
            }

            int newStock = product.getActualStock() - detailDTO.getQuantity();
            product.setActualStock(newStock);
            DetailSale saleDetail = new DetailSale();
            saleDetail.setProduct(product);
            saleDetail.setCantidad(detailDTO.getQuantity());
            saleDetail.setUnitPrice(detailDTO.getUnitPrice());
            saleDetail.setSubtotal(detailDTO.getSubtotal());
            
            saleDetail.setSale(newSale);
            
            newSale.getDetalles().add(saleDetail);
        }

        Sale savedSale = saleRepository.save(newSale);

        return convertSaleToDTO(savedSale);
    }

    private SaleDTO convertSaleToDTO(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setUserId(sale.getClient().getIdentification());
        dto.setClienteName(sale.getClient().getNames() + " " + sale.getClient().getLastNames());
        dto.setDate(sale.getDate());
        dto.setTaxes(sale.getTaxes());
        dto.setDiscounts(sale.getDiscounts());
        dto.setTotalValue(sale.getTotalValue());

        List<DetailSaleDTO> detailsDTO = sale.getDetalles().stream().map(detalle -> {
            DetailSaleDTO detDTO = new DetailSaleDTO();
            detDTO.setProductId(detalle.getProduct().getId());
            detDTO.setQuantity(detalle.getCantidad());
            detDTO.setUnitPrice(detalle.getUnitPrice());
            detDTO.setSubtotal(detalle.getSubtotal());
            return detDTO;
        }).collect(Collectors.toList());
        
        dto.setDetails(detailsDTO);

        return dto;
    }

    @Override
    public List<SaleDTO> listSales() {
        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .map(this::convertSaleToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));

        for (DetailSale detail : sale.getDetalles()) {
            Product product = detail.getProduct();
            
            int newStock = product.getActualStock() + detail.getCantidad();
            product.setActualStock(newStock);
        }
        
        saleRepository.delete(sale);
    }

    @Override
    @Transactional
    public SaleDTO upDateSale(Long id, SaleDTO saleDTO) {
        Sale actualSale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
        if (saleDTO.getUserId() != null &&
            !saleDTO.getUserId().equals(actualSale.getClient().getIdentification())) {
            
            User newClient = userRepository.findById(saleDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente (Tercero) no encontrado con ID: " + saleDTO.getUserId()));
            
            actualSale.setClient(newClient);
        }

        if (saleDTO.getTotalValue() != null) {
            actualSale.setTotalValue(saleDTO.getTotalValue());
        }
        if (saleDTO.getTaxes() != null) {
            actualSale.setTaxes(saleDTO.getTaxes());
        }
        if (saleDTO.getDiscounts() != null) {
            actualSale.setDiscounts(saleDTO.getDiscounts());
        }

        Sale savedSale = saleRepository.save(actualSale);
        return convertSaleToDTO(savedSale);
    }
}