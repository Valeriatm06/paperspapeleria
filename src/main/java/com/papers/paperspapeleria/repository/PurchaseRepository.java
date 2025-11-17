package com.papers.paperspapeleria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.papers.paperspapeleria.dto.PurchaseReportDTO;
import com.papers.paperspapeleria.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT new com.papers.paperspapeleria.dto.PurchaseReportDTO(" +
           "p.id, p.date, p.totalValue, u.names, u.identification) " +
           "FROM Purchase p JOIN p.supplier u") // 👈 Usa 'p.supplier' (como en tu entidad)
    List<PurchaseReportDTO> findAllPurchasesReport();
}
