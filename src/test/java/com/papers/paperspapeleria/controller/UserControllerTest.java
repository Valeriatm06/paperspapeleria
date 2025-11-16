package com.papers.paperspapeleria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void testCreateUser() throws Exception {
        UserDTO userToSend = new UserDTO();
        userToSend.setIdentificacion("12345678");
        userToSend.setNombres("Juan");
        userToSend.setApellidos("Gomez");
        userToSend.setEmail("juan@correo.com");
        userToSend.setRoles(Set.of("CLIENTE"));

        UserDTO userSaved = new UserDTO();
        userSaved.setIdentificacion("12345678");
        userSaved.setNombres("Juan");
        userSaved.setApellidos("Gomez");
        userSaved.setEmail("juan@correo.com");
        userSaved.setRoles(Set.of("CLIENTE"));
        userSaved.setActive(true);
        when(userService.createUser(any(UserDTO.class)))
                .thenReturn(userSaved);

        mockMvc.perform(
                post("/api/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userToSend))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.identificacion").value("12345678"))
        .andExpect(jsonPath("$.nombres").value("Juan"))
        .andExpect(jsonPath("$.active").value(true))
        .andExpect(jsonPath("$.roles[0]").value("CLIENTE"));
    }

    @Test
    void testListUsers() throws Exception {
        UserDTO user1 = new UserDTO();
        user1.setIdentificacion("111");
        user1.setNombres("Usuario Uno");

        UserDTO user2 = new UserDTO();
        user2.setIdentificacion("222");
        user2.setNombres("Usuario Dos");

        when(userService.listUsers())
                .thenReturn(List.of(user1, user2));
        mockMvc.perform(
                get("/api/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].identificacion").value("111"))
        .andExpect(jsonPath("$[0].nombres").value("Usuario Uno"));
    }

    @Test
    void testGetUserById() throws Exception {
        String userId = "12345";
        
        UserDTO userMock = new UserDTO();
        userMock.setIdentificacion(userId);
        userMock.setNombres("Usuario de Prueba");
        userMock.setEmail("prueba@correo.com");

        when(userService.getUserById(userId))
                .thenReturn(userMock);
        mockMvc.perform(
                get("/api/usuarios/" + userId) 
                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.identificacion").value(userId))
        .andExpect(jsonPath("$.nombres").value("Usuario de Prueba"));
    }

    @Test
    void testUpDateUser() throws Exception {
        String userId = "12345";
        UserDTO userUpDateDTO = new UserDTO();
        userUpDateDTO.setIdentificacion(userId);
        userUpDateDTO.setNombres("Nombre Editado");
        userUpDateDTO.setEmail("editado@correo.com");
        userUpDateDTO.setRoles(Set.of("CLIENTE"));
        when(userService.upDateUser(any(String.class), any(UserDTO.class)))
                .thenReturn(userUpDateDTO);
        mockMvc.perform(
                put("/api/usuarios/" + userId) 
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userUpDateDTO))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombres").value("Nombre Editado"))
        .andExpect(jsonPath("$.email").value("editado@correo.com"));
    }
}
