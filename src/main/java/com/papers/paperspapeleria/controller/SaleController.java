package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.dto.SaleDTO;
import com.papers.paperspapeleria.service.SaleService;

import java.util.List;

import jakarta.validation.Valid; // 💡 Importar para validación si se usa en SaleDTO
import jakarta.persistence.EntityNotFoundException; // 💡 Importar para manejo de excepciones
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // 💡 Importar ResponseEntity
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
    // 💡 CAMBIO: Retorna ResponseEntity para mejor control de errores
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleDTO saleDTO) {
        try {
            SaleDTO createdSale = saleService.createSale(saleDTO);
            return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            System.err.println("Error al crear venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si cliente/producto no encontrado
        } catch (IllegalArgumentException e) { // Para errores de stock
            System.err.println("Error al crear venta (validación): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 si stock insuficiente
        } catch (Exception e) {
            System.err.println("Error interno al crear venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 para otros errores
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SaleDTO> listarVentas() { // 💡 Este método llamará a getAllSales del servicio
        return saleService.getAllSales(); // 💡 CAMBIO: Llama a getAllSales
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarVenta(@PathVariable Long id) {
        saleService.deleteSale(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SaleDTO upDateSale(@PathVariable Long id, @RequestBody SaleDTO saleDTO) {
        return saleService.upDateSale(id, saleDTO);
    }
}