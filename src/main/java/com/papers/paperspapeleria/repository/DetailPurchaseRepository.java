package com.papers.paperspapeleria.repository;

import com.papers.paperspapeleria.dto.ExpensesPerCategoryDTO;
import com.papers.paperspapeleria.entity.DetailPurchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailPurchaseRepository extends JpaRepository<DetailPurchase, Long> {

   @Query("SELECT new com.papers.paperspapeleria.dto.ExpensesPerCategoryDTO(p.category, COALESCE(SUM(dp.subtotal), 0.0)) " +
       "FROM DetailPurchase dp " +
       "JOIN dp.product p " +
       "GROUP BY p.category")
           // (Esta consulta asume que tu entidad 'Product' tiene un campo 'category')
    List<ExpensesPerCategoryDTO> findExpensesPerCategory();
}