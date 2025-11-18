package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.ExpensesPerCategoryDTO;
import com.papers.paperspapeleria.dto.PurchaseInvoiceDetailDTO;
import com.papers.paperspapeleria.dto.PurchaseReportDTO;
import com.papers.paperspapeleria.dto.SaleInvoiceDetailDTO;
import com.papers.paperspapeleria.dto.SaleReportDTO;
import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.mapper.UserMapper;
import com.papers.paperspapeleria.repository.DetailPurchaseRepository;
import com.papers.paperspapeleria.repository.DetailSaleRepository;
import com.papers.paperspapeleria.repository.PurchaseRepository;
import com.papers.paperspapeleria.repository.SaleRepository;
import com.papers.paperspapeleria.repository.UserRepository; // 👈 2. Importar el Repo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private UserRepository userRepository; // 👈 3. Inyectar

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private DetailSaleRepository detailSaleRepository;

@Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private DetailPurchaseRepository detailPurchaseRepository;

    @Autowired
    private UserMapper userMapper; // 👈 4. Inyectar

    @Override
    @Transactional(readOnly = true) // 👈 5. Buena práctica para consultas
    public List<UserDTO> getListClients() {
       List<User> clientes = userRepository.findByRols_Name("CLIENTE"); 
        
        // 2. Convertir
        return clientes.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getListSupplier() {
        // 1. ⚠️ Lógica Real:
        List<User> proveedores = userRepository.findByRols_Name("PROVEEDOR"); 
        
        // 2. Convertir
        return proveedores.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpensesPerCategoryDTO> getExpensesPerCategory() {
        
        // 1. Ejecutamos la consulta y guardamos el resultado
        List<ExpensesPerCategoryDTO> resultados = detailPurchaseRepository.findExpensesPerCategory();
        
        // 2. ⚠️ ¡AQUÍ IMPRIMIMOS EL RESULTADO EN LA CONSOLA!
        System.out.println("--- DEBUG: Resultado de findExpensesPerCategory() ---");
        if (resultados == null || resultados.isEmpty()) {
            System.out.println("La consulta NO devolvió filas.");
        } else {
            // Iteramos por si hay varias categorías
            for (ExpensesPerCategoryDTO dto : resultados) {
                System.out.println("Categoría: " + dto.getCategory() + ", Total Gastado: " + dto.getTotalSpend());
            }
        }
        System.out.println("---------------------------------------------------");

        // 3. Devolvemos el resultado al controlador (como antes)
        return resultados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseReportDTO> getPurchaseReport() {
        System.out.println("--- DEBUG: Ejecutando getReporteCompras() ---");
        
        // 👈 CAMBIO: Llamamos al método corregido
        List<PurchaseReportDTO> resultados = purchaseRepository.findAllPurchasesReport(); 
        
        System.out.println("--- DEBUG: Resultado de getReporteCompras() ---");
        if (resultados == null || resultados.isEmpty()) {
            System.out.println("La consulta de Reporte de Compras NO devolvió filas.");
        } else {
            for (PurchaseReportDTO dto : resultados) {
                // 👈 CAMBIO: Usamos los nombres del DTO corregido
                System.out.println("Compra ID: " + dto.getId() + 
                                   ", Fecha: " + dto.getDate() + 
                                   ", Total: " + dto.getTotalValue() + 
                                   ", Proveedor: " + dto.getSupplierName());
            }
        }
        System.out.println("---------------------------------------------------");
        return resultados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseInvoiceDetailDTO> getReporteFacturasCompra() {
        System.out.println("--- DEBUG: Ejecutando getReporteFacturasCompra() ---");
        List<PurchaseInvoiceDetailDTO> resultados = detailPurchaseRepository.findFacturasCompraReport();
        System.out.println("Resultados encontrados: " + resultados.size());
        System.out.println("---------------------------------------------------");
        return resultados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleReportDTO> getReporteSales() {
        System.out.println("--- DEBUG: Ejecutando getReporteSales() ---");
        List<SaleReportDTO> resultados = saleRepository.findAllSalesReport();
        System.out.println("Resultados encontrados: " + resultados.size());
        System.out.println("---------------------------------------------------");
        return resultados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleInvoiceDetailDTO> getReporteFacturasVenta() {
        System.out.println("--- DEBUG: Ejecutando getReporteFacturasVenta() ---");
        List<SaleInvoiceDetailDTO> resultados = detailSaleRepository.findFacturasVentaReport();
        System.out.println("Resultados encontrados: " + resultados.size());
        System.out.println("---------------------------------------------------");
        return resultados;
    }


}