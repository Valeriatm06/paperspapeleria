package com.papers.paperspapeleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.papers.paperspapeleria.entity.DetailSale;

@Repository
public interface DeatilSaleRepository extends JpaRepository<DetailSale, Long>{

}
