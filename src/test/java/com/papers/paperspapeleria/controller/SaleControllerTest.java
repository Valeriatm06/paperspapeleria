package com.papers.paperspapeleria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papers.paperspapeleria.dto.DetailSaleDTO;
import com.papers.paperspapeleria.dto.SaleDTO;
import com.papers.paperspapeleria.service.SaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SaleController.class)
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaleService saleService;

    @Test
    void testCreateSale() throws Exception {
        DetailSaleDTO detail = new DetailSaleDTO();
        detail.setProductId(1L);
        detail.setQuantity(2);
        detail.setUnitPrice(2700.0);
        detail.setSubtotal(5400.0);

        SaleDTO saleToSend = new SaleDTO();
        saleToSend.setUserId("123456");
        saleToSend.setDate(LocalDateTime.now());
        saleToSend.setTotalValue(5400.0);
        saleToSend.setDetails(List.of(detail));

        when(saleService.createSale(any(SaleDTO.class)))
                .thenReturn(saleToSend);
        mockMvc.perform(
                post("/api/ventas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(saleToSend))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value("123456"))
        .andExpect(jsonPath("$.totalValue").value(5400.0))
        .andExpect(jsonPath("$.details[0].productId").value(1L));
    }

    @Test
    void testListSales() throws Exception {
        SaleDTO sale1 = new SaleDTO();
        sale1.setId(1L);
        sale1.setUserId("C1");
        sale1.setTotalValue(5400.0);

        SaleDTO sale2 = new SaleDTO();
        sale2.setId(2L);
        sale2.setUserId("C2");
        sale2.setTotalValue(8000.0);

        List<SaleDTO> listMock = List.of(sale1, sale2);

        when(saleService.listSales()).thenReturn(listMock);

        mockMvc.perform(
                get("/api/ventas")
                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].userId").value("C1"));
    }

    @Test
    void testDeleteSale() throws Exception {
        Long idSaleToDelete = 99L;
        mockMvc.perform(
                delete("/api/ventas/{id}", idSaleToDelete)
                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent());
        
        verify(saleService, times(1)).deleteSale(idSaleToDelete);
    }

}