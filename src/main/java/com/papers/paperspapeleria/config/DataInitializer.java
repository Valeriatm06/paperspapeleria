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
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Crear el ROL_ADMIN (si no existe)
        Rol adminRole;
        // (Tu código original usa "ADMINISTRADOR", lo cual está perfecto)
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
        String adminUsername = "admin";
        
        if (userRepository.findByIdentification(adminUsername).isEmpty()) {
            User adminUser = new User();
            adminUser.setIdentification(adminUsername); 
            adminUser.setActive(true);
            adminUser.setNames("Administrador");
            adminUser.setLastNames("Principal");
            adminUser.setEmail("admin@papers.com");
            adminUser.setIdType("CC");
            
            // (Tu código original para la contraseña del admin)
            adminUser.setPassword(passwordEncoder.encode("admin123")); 

            adminUser.getRols().add(adminRole);
            
            userRepository.save(adminUser);
            
            System.out.println(">>> Usuario 'admin' con contraseña 'admin123' creado <<<");
        }

        // --- 👈 INICIO DEL NUEVO BLOQUE ---
        // 3. Crear ROL_CLIENTE (si no existe)
        Rol clientRole;
        Optional<Rol> existingClientRole = rolRepository.findByName("CLIENTE"); 
        
        if (existingClientRole.isEmpty()) {
            clientRole = new Rol();
            clientRole.setId(2L); // 👈 Asumimos ID 2
            clientRole.setName("CLIENTE"); 
            clientRole = rolRepository.save(clientRole);
        } else {
            clientRole = existingClientRole.get();
        }

        // 4. Crear un Cliente de prueba (si no existe)
        String clientUsername = "cliente123";
        
        if (userRepository.findByIdentification(clientUsername).isEmpty()) {
            User clientUser = new User();
            clientUser.setIdentification(clientUsername);
            clientUser.setActive(true);
            clientUser.setNames("Cliente");
            clientUser.setLastNames("De Prueba");
            clientUser.setEmail("cliente@papers.com");
            clientUser.setIdType("CC");
            
            // 5. 👈 ESTA ES LA CLAVE:
            // Los clientes no tienen contraseña
            clientUser.setPassword(null); 

            // 6. Asignar el rol "CLIENTE"
            clientUser.getRols().add(clientRole);
            
            userRepository.save(clientUser);
            System.out.println(">>> Usuario 'cliente123' (CLIENTE) creado <<<");
        }
        
        Rol supplierRole;
    Optional<Rol> existingSupplierRole = rolRepository.findByName("PROVEEDOR");
    
    if (existingSupplierRole.isEmpty()) {
        supplierRole = new Rol();
        supplierRole.setId(3L); // 👈 Asumimos ID 3
        supplierRole.setName("PROVEEDOR");
        supplierRole = rolRepository.save(supplierRole);
    } else {
        supplierRole = existingSupplierRole.get();
    }

    // 2. Crear un Proveedor de prueba
    String supplierUsername = "prov123";
    if (userRepository.findByIdentification(supplierUsername).isEmpty()) {
        User supplierUser = new User();
        supplierUser.setIdentification(supplierUsername);
        supplierUser.setNames("Proveedor De Prueba");
        supplierUser.setLastNames("S.A.S");
        supplierUser.setEmail("compras@proveedor.com");
        supplierUser.setPassword(null); // 👈 Sin contraseña
        supplierUser.getRols().add(supplierRole);
        userRepository.save(supplierUser);
        System.out.println(">>> Usuario 'prov123' (PROVEEDOR) creado <<<");
    }
}
}