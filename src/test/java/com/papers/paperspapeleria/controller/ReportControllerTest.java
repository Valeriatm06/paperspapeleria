package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.config.SecurityConfig;
import com.papers.paperspapeleria.dto.UserDTO; // Asumimos que tienes un UserDTO
import com.papers.paperspapeleria.security.JwtAuthFilter;
import com.papers.paperspapeleria.security.UserDetailsServiceImpl;
import com.papers.paperspapeleria.service.ReportService; // 👈 Servicio que crearemos
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class) // 👈 Controlador que crearemos
@Import({SecurityConfig.class, UserDetailsServiceImpl.class, JwtAuthFilter.class}) // Importamos toda la seguridad
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService; // 👈 Mock del servicio de reportes
    
    // Mocks de seguridad (necesarios porque SecurityConfig los usa)
    @MockBean private com.papers.paperspapeleria.repository.UserRepository userRepository;
    @MockBean private com.papers.paperspapeleria.repository.RolRepository rolRepository;
    @MockBean private com.papers.paperspapeleria.security.JwtProvider jwtProvider;

    @Test
    @WithMockUser // 👈 Simula que estamos logueados (evita 403)
    void testGetListClients() throws Exception {
        // 1. Arrange
        UserDTO cliente1 = new UserDTO();
        cliente1.setIdentificacion("123");
        cliente1.setNombres("Cliente Prueba");

        UserDTO cliente2 = new UserDTO();
        cliente2.setIdentificacion("456");
        cliente2.setNombres("Otro Cliente");

        List<UserDTO> mockResponse = List.of(cliente1, cliente2);

        when(reportService.getListClients()).thenReturn(mockResponse); // 👈 Fallará (no existe)

        // 2. Act & 3. Assert
        mockMvc.perform(
                get("/api/reportes/listado-clientes") // 👈 Endpoint que crearemos
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk()) // Espera 200 OK
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].nombres").value("Cliente Prueba"));
    }

    // En ReportControllerTest.java

    @Test
    @WithMockUser // Simula logueado
    void testGetListadoProveedores() throws Exception {
        // 1. Arrange
        UserDTO proveedor1 = new UserDTO();
        proveedor1.setIdentificacion("9001");
        proveedor1.setNombres("Proveedor Prueba");

        List<UserDTO> mockResponse = List.of(proveedor1);

        // 2. Simular el nuevo método del servicio
        when(reportService.getListSupplier()).thenReturn(mockResponse); // 👈 Fallará (no existe)

        // 3. Act & Assert
        mockMvc.perform(
                get("/api/reportes/listado-proveedores") // 👈 Endpoint nuevo
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].nombres").value("Proveedor Prueba"));
    }
}