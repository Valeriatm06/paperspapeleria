package com.papers.paperspapeleria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papers.paperspapeleria.dto.DetailPurchaseDTO;
import com.papers.paperspapeleria.dto.PurchaseDTO;
import com.papers.paperspapeleria.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService; // Simulación del Servicio

    @Autowired
    private ObjectMapper objectMapper;

    private final Long PURCHASE_ID = 1L;

    private final String SUPPLIER_ID = "99887766"; 
    
    private PurchaseDTO simplePurchaseDTO;
    private PurchaseDTO updatedPurchaseDTO;

    @BeforeEach
    void setUp() {
        // Detalle de Compra
        DetailPurchaseDTO detailDTO = new DetailPurchaseDTO();
        detailDTO.setProductId(101L); 
        detailDTO.setQuantity(5);
        detailDTO.setUnitPrice(10.0);
        detailDTO.setSubtotal(50.0);

        // DTO Base (usado para la creación y búsqueda)
        simplePurchaseDTO = new PurchaseDTO();
        simplePurchaseDTO.setId(PURCHASE_ID); 
        simplePurchaseDTO.setUserId(SUPPLIER_ID); // Se usa String
        simplePurchaseDTO.setTotalValue(50.0);
        simplePurchaseDTO.setDetails(List.of(detailDTO));
        
        // DTO para la prueba de Actualización (PUT)
        updatedPurchaseDTO = new PurchaseDTO();
        updatedPurchaseDTO.setId(PURCHASE_ID);
        updatedPurchaseDTO.setUserId("NUEVO_PROVEEDOR_ID"); // Se usa String
        updatedPurchaseDTO.setTotalValue(10000.0);
        updatedPurchaseDTO.setTaxes(1600.0);
        updatedPurchaseDTO.setDetails(null);
    }
    
    // --- C: Create (POST /api/purchases) ---
    @Test
    void testCreatePurchase() throws Exception { 
        when(purchaseService.createPurchase(any(PurchaseDTO.class))).thenReturn(simplePurchaseDTO);

        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(simplePurchaseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PURCHASE_ID))
                .andExpect(jsonPath("$.userId").value(SUPPLIER_ID)) // Verifica String
                .andExpect(jsonPath("$.totalValue").value(50.0))
                .andExpect(jsonPath("$.details[0].productId").value(101L));
    }

    // --- R: Read (GET /api/purchases) ---
    @Test
    void testGetAllPurchases() throws Exception { 
        PurchaseDTO purchase2 = new PurchaseDTO();
        purchase2.setId(2L);
        purchase2.setUserId("7777777"); // Se usa String
        purchase2.setTotalValue(8000.0);

        List<PurchaseDTO> listMock = List.of(simplePurchaseDTO, purchase2);

        when(purchaseService.getAllPurchases()).thenReturn(listMock);

        mockMvc.perform(get("/api/compras").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].userId").value(SUPPLIER_ID)); // Verifica String
    }

    // --- R: Read (GET /api/purchases/{id}) ---
    @Test
    void testGetPurchaseById() throws Exception {
        when(purchaseService.getPurchaseById(PURCHASE_ID)).thenReturn(simplePurchaseDTO);

        mockMvc.perform(get("/api/compras/{id}", PURCHASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.id").value(PURCHASE_ID))
                .andExpect(jsonPath("$.userId").value(SUPPLIER_ID)); // Verifica String
    }


    // --- U: Update (PUT /api/purchases/{id}) ---
    @Test
    void testUpdatePurchase() throws Exception { 
        
        when(purchaseService.updatePurchase(eq(PURCHASE_ID), any(PurchaseDTO.class)))
                .thenReturn(updatedPurchaseDTO);

        mockMvc.perform(
                put("/api/compras/{id}", PURCHASE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPurchaseDTO))
        )
        .andExpect(status().isOk()) 
        .andExpect(jsonPath("$.userId").value("NUEVO_PROVEEDOR_ID")); // Verifica String
    }

    // --- D: Delete (DELETE /api/purchases/{id}) ---
    @Test
    void testDeletePurchase() throws Exception { 
        
        Long idPurchaseToDelete = 99L;
        doNothing().when(purchaseService).deletePurchase(idPurchaseToDelete);
        
        mockMvc.perform(
                delete("/api/compras/{id}", idPurchaseToDelete)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent());
        
        verify(purchaseService, times(1)).deletePurchase(idPurchaseToDelete);
    }
}