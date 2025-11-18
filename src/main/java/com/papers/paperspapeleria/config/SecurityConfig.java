package com.papers.paperspapeleria.config;

import com.papers.paperspapeleria.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Set;
import java.util.stream.Collectors;
import com.papers.paperspapeleria.security.JwtAuthFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // Bean del Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para el AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Bean del Filtro de Seguridad principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // Habilita CORS con configuración por defecto
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF (común en APIs REST con JWT)
            .authorizeHttpRequests(auth -> auth
                // --- Rutas de Autenticación (Públicas) ---
                .requestMatchers("/api/auth/**").permitAll() 
                
                // --- Rutas de Compras ---
                // Permitir todas las operaciones de Compras sin autenticación (TEMPORAL y MUY PELIGROSO)
                .requestMatchers("/api/compras", "/api/compras/**").permitAll() 
                
                // --- Rutas de Productos ---
                // Permitir todas las operaciones de Productos sin autenticación (TEMPORAL y MUY PELIGROSO)
                .requestMatchers("/api/productos", "/api/productos/**").permitAll() 
                
                // --- Rutas de Usuarios ---
                // Permitir cargar proveedores sin autenticación
                .requestMatchers("/api/usuarios/rol/PROVEEDOR").permitAll()
                // Si quieres que el /api/usuarios general (y por ID) también sea accesible para GET sin autenticación
                .requestMatchers(HttpMethod.GET, "/api/usuarios", "/api/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/usuarios", "/api/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios", "/api/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/**").permitAll()
                // Si quieres permitir POST/PUT/DELETE para usuarios sin autenticación (MUY PELIGROSO)
                // .requestMatchers("/api/usuarios", "/api/usuarios/**").permitAll()

                // --- Rutas de Ventas ---
                // Permitir todas las operaciones de Ventas sin autenticación (TEMPORAL y MUY PELIGROSO)
                .requestMatchers("/api/ventas", "/api/ventas/**").permitAll()
                
                // --- Cualquier otra ruta ---
                // Cualquier otra petición no especificada explícitamente, SÍ requiere autenticación.
                // Esta regla está al final, por lo que solo se aplica si las anteriores no hicieron match.
                .anyRequest().authenticated() 
            )
            // Configuración de la política de sesión a STATELESS para APIs REST (sin estado de sesión)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // Añadir el filtro JWT antes del filtro de autenticación de usuario y contraseña
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}