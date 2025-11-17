package com.papers.paperspapeleria.mapper;

import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    /**
     * Convierte una Entidad User a un UserDTO.
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        // Usamos los nombres de campo de tu entidad User.java
        dto.setIdentificacion(user.getIdentification());
        dto.setNombres(user.getNames());
        dto.setApellidos(user.getLastNames());
        dto.setEmail(user.getEmail());
        dto.setCiudad(user.getCity());
        dto.setDireccion(user.getAddress());
        dto.setActive(user.isActive());
        
        // Convertir el Set<Rol> a un Set<String>
        dto.setRoles(user.getRols().stream()
                .map(rol -> rol.getName()) // Asumiendo que Rol tiene un .getName()
                .collect(Collectors.toSet()));

        return dto;
    }
}