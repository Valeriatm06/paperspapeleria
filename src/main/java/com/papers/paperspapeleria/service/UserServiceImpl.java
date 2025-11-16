package com.papers.paperspapeleria.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.entity.Rol;
import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.repository.RolRepository;
import com.papers.paperspapeleria.repository.UserRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsById(userDTO.getIdentificacion())) {
            throw new EntityExistsException("Ya existe un user con la identificación: " + userDTO.getIdentificacion());
        }
        Set<String> rolesSolicitados = userDTO.getRoles();
        if (rolesSolicitados.contains("ADMINISTRADOR") && rolesSolicitados.contains("EMPLEADO")) {
            throw new IllegalArgumentException("Error: Una persona no puede ser Administrador y Empleado al mismo tiempo.");
        }
        User newUser = new User();
        newUser.setIdentification(userDTO.getIdentificacion());
        newUser.setPersonType(userDTO.getTipoPersona());
        newUser.setIdType(userDTO.getTipoIdentificacion());
        newUser.setNames(userDTO.getNombres());
        newUser.setLastNames(userDTO.getApellidos());
        newUser.setCity(userDTO.getCiudad());
        newUser.setAddress(userDTO.getDireccion());
        newUser.setContactName(userDTO.getNombresContacto());
        newUser.setContactLastName(userDTO.getApellidosContacto());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getTelefono());
        newUser.setActive(true);

        Set<Rol> rolesEntidad = new HashSet<>();
        for (String rolName : rolesSolicitados) {
            Rol rol = rolRepository.findByName(rolName)
                    .orElseThrow(() -> new EntityNotFoundException("El rol '" + rolName + "' no existe."));
            rolesEntidad.add(rol);
        }
        newUser.setRols(rolesEntidad);
        
        if (rolesSolicitados.contains("EMPLEADO") || rolesSolicitados.contains("ADMINISTRADOR")) {
            if (userDTO.getUsername() == null || userDTO.getPassword() == null) {
                throw new IllegalArgumentException("El username y password son obligatorios para Empleados o Administradores.");
            }
            newUser.setUsername(userDTO.getUsername());
            newUser.setPassword(userDTO.getPassword());
        }

        User savedUser = userRepository.save(newUser);

        return convertTOEntityDTO(savedUser);
    }
    
    private UserDTO convertTOEntityDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setIdentificacion(user.getIdentification());
        dto.setTipoPersona(user.getPersonType());
        dto.setTipoIdentificacion(user.getIdType());
        dto.setNombres(user.getNames());
        dto.setApellidos(user.getLastNames());
        dto.setCiudad(user.getCity());
        dto.setDireccion(user.getAddress());
        dto.setNombresContacto(user.getContactName());
        dto.setApellidosContacto(user.getContactLastName());
        dto.setEmail(user.getEmail());
        dto.setTelefono(user.getPhoneNumber());
        dto.setUsername(user.getUsername());
        dto.setPassword(null); 
        dto.setActive(user.isActive());

        dto.setRoles(
            user.getRols().stream()
                    .map(Rol::getName)
                    .collect(Collectors.toSet())
        );

        return dto;
    }

    @Override
    public List<UserDTO> listUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertTOEntityDTO)
                .collect(Collectors.toList());
    }

}
