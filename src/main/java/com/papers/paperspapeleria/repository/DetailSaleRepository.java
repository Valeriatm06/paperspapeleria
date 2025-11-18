package com.papers.paperspapeleria.repository;

import com.papers.paperspapeleria.dto.SaleInvoiceDetailDTO;
import com.papers.paperspapeleria.entity.DetailSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailSaleRepository extends JpaRepository<DetailSale, Long> {

    // 2. ⚠️ AÑADIR NUEVA CONSULTA: Detalle de Facturas de Venta
    @Query("SELECT new com.papers.paperspapeleria.dto.SaleInvoiceDetailDTO(" +
           "s.id, s.date, cli.names, prod.reference, prod.name, ds.cantidad, ds.unitPrice, ds.subtotal) " +
           "FROM DetailSale ds " +
           "JOIN ds.sale s " + // 👈 Unir con la Venta (Sale)
           "JOIN s.client cli " + // 👈 Unir la Venta con el Cliente (User)
           "JOIN ds.product prod " + // 👈 Unir el Detalle con el Producto (Product)
           "ORDER BY s.date DESC, prod.name ASC") // Ordenar por fecha y luego por nombre
    List<SaleInvoiceDetailDTO> findFacturasVentaReport();
}