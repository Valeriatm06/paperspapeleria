package com.papers.paperspapeleria.config;

import com.papers.paperspapeleria.entity.Rol; // Importa tu entidad Rol
import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.repository.RolRepository; // Usa el nuevo RolRepository
import com.papers.paperspapeleria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolRepository rolRepository; // 👈 Ya no usamos UserRoleRepository
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Crear el ROL_ADMIN (si no existe)
        Rol adminRole;
        Optional<Rol> existingRole = rolRepository.findByName("ADMINISTRADOR");
        
        if (existingRole.isEmpty()) {
            adminRole = new Rol();
            adminRole.setId(1L); // Asumimos ID 1 para Admin
            adminRole.setName("ADMINISTRADOR");
            adminRole = rolRepository.save(adminRole);
        } else {
            adminRole = existingRole.get();
        }

        // 2. Crear el Usuario ADMIN (si no existe)
        String adminUsername = "admin"; // Esta es la 'identification'
        
        if (userRepository.findByIdentification(adminUsername).isEmpty()) {
            User adminUser = new User();
            // ⚠️ USA LOS NOMBRES DE TUS CAMPOS JAVA
            adminUser.setIdentification(adminUsername); 
            adminUser.setActive(true);
            adminUser.setNames("Administrador");
            adminUser.setLastNames("Principal");
            adminUser.setEmail("admin@papers.com");
            adminUser.setIdType("CC"); // 'idType' en lugar de 'tipoIdentificacion'
            
            // 3. Encriptar la contraseña "admin123"
            adminUser.setPassword(passwordEncoder.encode("admin123")); // 'password' en lugar de 'passwordHash'

            // 4. Asignar el rol (JPA maneja la tabla users_roles)
            adminUser.getRols().add(adminRole); // 👈 Asignación directa
            
            userRepository.save(adminUser);
            
            System.out.println(">>> Usuario 'admin' con contraseña 'admin123' creado <<<");
        }
    }
}