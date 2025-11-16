package com.papers.paperspapeleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.papers.paperspapeleria.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
