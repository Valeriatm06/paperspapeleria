package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.mapper.UserMapper; // 👈 1. Importar el Mapper
import com.papers.paperspapeleria.repository.UserRepository; // 👈 2. Importar el Repo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private UserRepository userRepository; // 👈 3. Inyectar

    @Autowired
    private UserMapper userMapper; // 👈 4. Inyectar

    @Override
    @Transactional(readOnly = true) // 👈 5. Buena práctica para consultas
    public List<UserDTO> getListClients() {
       List<User> clientes = userRepository.findByRols_Name("CLIENTE"); 
        
        // 2. Convertir
        return clientes.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}