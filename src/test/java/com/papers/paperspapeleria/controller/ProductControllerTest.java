package com.papers.paperspapeleria.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.papers.paperspapeleria.dto.ProductDetailDTO;
import com.papers.paperspapeleria.service.ProductService;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListProducts() throws Exception {
        mockMvc.perform(
                get("/api/productos")
        )
        .andExpect(status().isOk());
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDetailDTO mockProduct = new ProductDetailDTO();
        mockProduct.setId(1L);
        mockProduct.setName("Cuaderno Norma Test");
        mockProduct.setReference("REF-123");
        when(productoService.getProductById(1L))
                .thenReturn(mockProduct);
        mockMvc.perform(
                get("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Cuaderno Norma Test"))
        .andExpect(jsonPath("$.reference").value("REF-123"));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDetailDTO productToSend = new ProductDetailDTO();
        productToSend.setName("Producto Nuevo");
        productToSend.setReference("REF-NUEVO");
        productToSend.setSalePrice(1000.0);

        ProductDetailDTO savedProduct = new ProductDetailDTO();
        savedProduct.setId(1L);
        savedProduct.setName("Producto Nuevo");
        savedProduct.setReference("REF-NUEVO");
        savedProduct.setSalePrice(1000.0);

        when(productoService.createProduct(any(ProductDetailDTO.class)))
                .thenReturn(savedProduct);
        mockMvc.perform(
                post("/api/productos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productToSend))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Producto Nuevo"))
        .andExpect(jsonPath("$.reference").value("REF-NUEVO"));
    }
}