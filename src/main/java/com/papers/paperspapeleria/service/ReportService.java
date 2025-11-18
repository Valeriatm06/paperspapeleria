package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.ExpensesPerCategoryDTO;
import com.papers.paperspapeleria.dto.PurchaseInvoiceDetailDTO;
import com.papers.paperspapeleria.dto.PurchaseReportDTO;
import com.papers.paperspapeleria.dto.SaleInvoiceDetailDTO;
import com.papers.paperspapeleria.dto.SaleReportDTO;
import com.papers.paperspapeleria.dto.UserDTO;

import java.util.List;

public interface ReportService {
    List<UserDTO> getListClients();

    List<UserDTO> getListSupplier();

    List<ExpensesPerCategoryDTO> getExpensesPerCategory();

    List<PurchaseReportDTO> getPurchaseReport();

    List<PurchaseInvoiceDetailDTO> getReporteFacturasCompra();

    List<SaleReportDTO> getReporteSales();

    List<SaleInvoiceDetailDTO> getReporteFacturasVenta();
}