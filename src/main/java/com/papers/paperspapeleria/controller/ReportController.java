package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes") // 👈 Mapeo base
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/listado-clientes") // 👈 Endpoint específico
    public ResponseEntity<List<UserDTO>> getListClients() {
        List<UserDTO> clientes = reportService.getListClients();
        return ResponseEntity.ok(clientes);
    }
}