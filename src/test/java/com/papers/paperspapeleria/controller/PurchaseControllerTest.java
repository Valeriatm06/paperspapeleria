package com.papers.paperspapeleria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papers.paperspapeleria.dto.DetailPurchaseDTO;
import com.papers.paperspapeleria.dto.PurchaseDTO;
import com.papers.paperspapeleria.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService; // Simulación del Servicio

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePruchase() throws Exception {
        DetailPurchaseDTO detailDTO = new DetailPurchaseDTO();
        detailDTO.setProductId(101L);
        detailDTO.setQuantity(5);
        detailDTO.setUnitPrice(10.0);
        detailDTO.setSubtotal(50.0);

        PurchaseDTO inputDTO = new PurchaseDTO();
        inputDTO.setUserId("12345"); 
        inputDTO.setTotalValue(50.0);
        inputDTO.setDetails(List.of(detailDTO));

        PurchaseDTO outputDTO = new PurchaseDTO();
        outputDTO.setId(1L);
        outputDTO.setUserId("12345");
        outputDTO.setTotalValue(50.0);
        outputDTO.setDetails(List.of(detailDTO));

        when(purchaseService.createPurchase(any(PurchaseDTO.class))).thenReturn(outputDTO);

        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value("12345"))
                .andExpect(jsonPath("$.totalValue").value(50.0))
                .andExpect(jsonPath("$.details[0].productId").value(101L));
    }
}