package com.papers.paperspapeleria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papers.paperspapeleria.config.SecurityConfig;
import com.papers.paperspapeleria.dto.LoginRequest;
import com.papers.paperspapeleria.dto.LoginResponse;
import com.papers.paperspapeleria.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService; 

    @Test
    void testLogin_DebeDevolver200OKYToken() throws Exception {
        // --- 1. Arrange (Preparar) ---
        
        // A. Solicitud de Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPass");

        LoginResponse loginResponse = new LoginResponse("mock.jwt.token", "testUser");

        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.authToken").value("mock.jwt.token"))
        .andExpect(jsonPath("$.username").value("testUser"));
    }
}