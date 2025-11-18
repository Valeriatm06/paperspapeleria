package com.papers.paperspapeleria.dto;

import lombok.Data;

@Data
public class ExpensesPerCategoryDTO {
    private String category;
    private Double totalSpend;

    // Constructor para que la consulta JPQL funcione
    public ExpensesPerCategoryDTO(String category, Double totalSpend) {
        this.category = category;
        this.totalSpend = totalSpend;
    }
}