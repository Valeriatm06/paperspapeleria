package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.DetailSaleDTO;
import com.papers.paperspapeleria.dto.SaleDTO;
import com.papers.paperspapeleria.entity.*;
import com.papers.paperspapeleria.repository.ProductRepository;
import com.papers.paperspapeleria.repository.SaleRepository;
import com.papers.paperspapeleria.repository.UserRepository; // Importar UserRepository

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList; 
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository; // 💡 AÑADIDO: Necesitas el UserRepository para buscar al cliente
    private final ProductRepository productRepository;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository,
                           UserRepository userRepository, // 💡 AÑADIDO: Inyectar UserRepository
                           ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository; // 💡 AÑADIDO: Asignar
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public SaleDTO createSale(SaleDTO saleDTO) {
        // 💡 CAMBIO: Buscar cliente por su IDENTIFICACIÓN (String), como tu SaleDTO.userId lo indica
        User cliente = userRepository.findById(saleDTO.getUserId()) 
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + saleDTO.getUserId()));

        Sale newSale = new Sale();
        newSale.setClient(cliente);
        newSale.setDate(LocalDateTime.now()); // Generar la fecha y hora actual
        newSale.setTaxes(saleDTO.getTaxes() != null ? saleDTO.getTaxes() : 0.0); // Manejo de nulos
        newSale.setDiscounts(saleDTO.getDiscounts() != null ? saleDTO.getDiscounts() : 0.0); // Manejo de nulos
        newSale.setTotalValue(saleDTO.getTotalValue());

        List<DetailSale> saleDetails = new ArrayList<>(); 
        for (DetailSaleDTO detailDTO : saleDTO.getDetails()) { 
            
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + detailDTO.getProductId()));

            // 💡 Lógica de stock: Verificar y restar del stock del producto
            // Manteniendo tu campo 'actualStock'
            if (product.getActualStock() < detailDTO.getQuantity()) { 
                throw new IllegalArgumentException("No hay stock suficiente para el producto: " + product.getName() + ". Stock actual: " + product.getActualStock());
            }

            int newStock = product.getActualStock() - detailDTO.getQuantity();
            product.setActualStock(newStock);
            productRepository.save(product); // Guardar el producto con el stock actualizado
            
            DetailSale saleDetail = new DetailSale();
            saleDetail.setProduct(product);
            saleDetail.setCantidad(detailDTO.getQuantity()); // El campo de tu entidad es 'cantidad'
            saleDetail.setUnitPrice(detailDTO.getUnitPrice());
            saleDetail.setSubtotal(detailDTO.getSubtotal());
            
            saleDetail.setSale(newSale); // Vinculamos el detalle a la venta
            
            saleDetails.add(saleDetail);
        }
        newSale.setDetalles(saleDetails); // Asignar todos los detalles a la venta

        Sale savedSale = saleRepository.save(newSale);

        return convertSaleToDTO(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    // 💡 CAMBIO: Nombre del método según la interfaz
    public List<SaleDTO> getAllSales() { 
        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .map(this::convertSaleToDTO)
                .collect(Collectors.toList());
    }

    // Método privado de ayuda para convertir Entidad a DTO (lo tienes bien, solo un pequeño ajuste para productName)
    private SaleDTO convertSaleToDTO(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setUserId(sale.getClient().getIdentification()); // Mantén la identificación si tu DTO la espera como String
        dto.setClienteName(sale.getClient().getNames() + " " + sale.getClient().getLastNames()); // Asegúrate de tener getApellidos()
        
        dto.setDate(sale.getDate());
        dto.setTaxes(sale.getTaxes());
        dto.setDiscounts(sale.getDiscounts());
        dto.setTotalValue(sale.getTotalValue());

        List<DetailSaleDTO> detailsDTO = sale.getDetalles().stream().map(detalle -> {
            DetailSaleDTO detDTO = new DetailSaleDTO();
            detDTO.setProductId(detalle.getProduct().getId());
            detDTO.setProductName(detalle.getProduct().getName()); // 💡 ¡AÑADIDO: Mapear el nombre del producto!
            detDTO.setQuantity(detalle.getCantidad());
            detDTO.setUnitPrice(detalle.getUnitPrice());
            detDTO.setSubtotal(detalle.getSubtotal());
            return detDTO;
        }).collect(Collectors.toList());
        
        dto.setDetails(detailsDTO);

        return dto;
    }

    // --- Tus métodos existentes de deleteSale y upDateSale (los mantengo sin cambios) ---
    @Override
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));

        for (DetailSale detail : sale.getDetalles()) {
            Product product = detail.getProduct();
            int newStock = product.getActualStock() + detail.getCantidad();
            product.setActualStock(newStock);
            productRepository.save(product);
        }
        
        saleRepository.delete(sale);
    }

    @Override
    @Transactional
    public SaleDTO upDateSale(Long id, SaleDTO saleDTO) {
        Sale actualSale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + id));
        
        if (saleDTO.getUserId() != null &&
            !saleDTO.getUserId().equals(actualSale.getClient().getIdentification())) { // Usar getIdentificacion()
            
            // 💡 AJUSTE: Buscar cliente por identificacion (String)
            User newClient = userRepository.findByIdentification(saleDTO.getUserId()) 
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + saleDTO.getUserId()));
            
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