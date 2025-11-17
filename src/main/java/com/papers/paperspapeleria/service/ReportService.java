package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.SaleDTO;
import com.papers.paperspapeleria.dto.UserDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<UserDTO> getListClients();

    List<UserDTO> getListSupplier();
}