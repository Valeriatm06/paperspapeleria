package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.mapper.UserMapper;
import com.papers.paperspapeleria.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 💡 Importar PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // 💡 Inyectar PasswordEncoder

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        // Verificar si ya existe un usuario con la misma identificación o username/email
        if (userRepository.findByIdentification(userDTO.getIdentificacion()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con la identificación: " + userDTO.getIdentificacion());
        }
        if (userDTO.getUsername() != null && userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
             throw new IllegalArgumentException("Ya existe un usuario con el nombre de usuario: " + userDTO.getUsername());
        }
        if (userDTO.getEmail() != null && userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
             throw new IllegalArgumentException("Ya existe un usuario con el email: " + userDTO.getEmail());
        }

        User user = userMapper.toEntity(userDTO);
        
        // 💡 Encriptar la contraseña si se proporciona
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else if (userDTO.getRoles().contains("EMPLEADO")) {
            // Si es un empleado, la contraseña es obligatoria al crear
             throw new IllegalArgumentException("La contraseña es obligatoria para usuarios tipo EMPLEADO.");
        }
        // Para clientes/proveedores que no tienen credenciales de login, la contraseña puede ser null

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        // Filtrar aquí si no quieres el rol "ADMIN" en la lista general
        return userRepository.findAll().stream()
                .filter(user -> user.getRols().stream().noneMatch(rol -> rol.getName().equals("ADMIN"))) // 💡 Filtrar ADMIN
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(String id) {
        return userRepository.findByIdentification(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public UserDTO upDateUser(String id, UserDTO userDTO) {
        User existingUser = userRepository.findByIdentification(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        
        // Verificar si el username o email ya existen en otro usuario (que no sea el actual)
        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso por otro usuario.");
            }
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("El email ya está en uso por otro usuario.");
            }
        }

        // 💡 Actualizar la contraseña si se proporciona en el DTO
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // 💡 Usar el método updateEntityFromDTO para actualizar los otros campos
        userMapper.updateEntityFromDTO(userDTO, existingUser);

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findByIdentification(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findUsersByRole(String roleName) {
        // 💡 IMPLEMENTACIÓN: Buscar por nombre de rol y mapear a DTOs
        // Asegúrate de que no estás filtrando 'ADMIN' si lo que buscas es 'ADMIN'
        return userRepository.findByRols_Name(roleName).stream()
                .filter(user -> user.getRols().stream().noneMatch(rol -> rol.getName().equals("ADMIN") && !roleName.equals("ADMIN"))) // No mostrar admin a menos que se busque admin explícitamente
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}