package com.papers.paperspapeleria.security;

import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca por 'identification' (que es el username)
        User user = userRepository.findByIdentification(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + username));
        
        // Convierte el Set<Rol> en una colección de GrantedAuthority
        Set<GrantedAuthority> authorities = user.getRols().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                .collect(Collectors.toSet());
        
        // Devuelve el usuario de Spring Security
        return new org.springframework.security.core.userdetails.User(
            user.getIdentification(),
            user.getPassword(),
            authorities
        );
    }
}