package com.papers.paperspapeleria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.papers.paperspapeleria.dto.SaleReportDTO;
import com.papers.paperspapeleria.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{

    @Query("SELECT new com.papers.paperspapeleria.dto.SaleReportDTO(" +
           "s.id, s.date, c.names, c.identification, s.totalValue, s.taxes, s.discounts) " +
           "FROM Sale s JOIN s.client c " +
           "ORDER BY s.date DESC")
    List<SaleReportDTO> findAllSalesReport();
}
