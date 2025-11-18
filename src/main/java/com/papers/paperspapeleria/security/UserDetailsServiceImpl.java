package com.papers.paperspapeleria.security;

import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException; // 👈 1. Asegúrate de tener este import
import org.slf4j.Logger; // 👈 2. Importa Logger
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service // 👈 ¡Importante! Marcarla como un servicio
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Busca al usuario (como antes)
        User user = userRepository.findByIdentification(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + username));
        
        // 2. CAMBIO DE SEGURIDAD (Ahora 'logger.warn' funcionará)
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // Esta línea ahora es válida
            logger.warn("Intento de login de un usuario sin contraseña (probablemente un cliente): {}", username);
            throw new UsernameNotFoundException("Usuario no habilitado para login: " + username);
        }
        
        // 3. Convierte el Set<Rol> (como antes)
        Set<GrantedAuthority> authorities = user.getRols().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                .collect(Collectors.toSet());
        
        // 4. Devuelve el usuario de Spring (como antes)
        return new org.springframework.security.core.userdetails.User(
            user.getIdentification(),
            user.getPassword(),
            authorities
        );
    }
}