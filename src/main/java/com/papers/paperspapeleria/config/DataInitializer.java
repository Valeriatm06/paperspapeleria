package com.papers.paperspapeleria.config;

import com.papers.paperspapeleria.entity.Rol;
import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.repository.RolRepository;
import com.papers.paperspapeleria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set; // Asegúrate de tener este import

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
        
        // --- 1. Crear Roles ---
        Rol adminRole = createRoleIfNotExists("ADMINISTRADOR");
        Rol clientRole = createRoleIfNotExists("CLIENTE");
        Rol supplierRole = createRoleIfNotExists("PROVEEDOR");
        Rol employeeRole = createRoleIfNotExists("EMPLEADO");


        // --- 2. Crear Usuario Administrador (con login) ---
        if (userRepository.findByIdentification("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setIdentification("admin"); 
            adminUser.setActive(true);
            adminUser.setNames("Administrador");
            adminUser.setLastNames("Principal");
            adminUser.setEmail("admin@papers.com");
            adminUser.setIdType("CC");
            adminUser.setUsername("admin"); // Username para login
            adminUser.setPassword(passwordEncoder.encode("admin123")); 
            
            // Asignar el rol
            adminUser.setRols(Set.of(adminRole)); // Asignamos el rol de Admin
            
            userRepository.save(adminUser);
            System.out.println(">>> Usuario 'admin' con contraseña 'admin123' [ADMINISTRADOR] creado <<<");
        }

        // --- 3. Crear Cliente de Prueba (sin login) ---
        if (userRepository.findByIdentification("cliente123").isEmpty()) {
            User clientUser = new User();
            clientUser.setIdentification("cliente123");
            clientUser.setActive(true);
            clientUser.setNames("Cliente");
            clientUser.setLastNames("De Prueba");
            clientUser.setEmail("cliente@papers.com");
            clientUser.setIdType("CC");
            clientUser.setPassword(null); // Sin contraseña
            
            clientUser.setRols(Set.of(clientRole)); // Asignamos el rol de Cliente
            
            userRepository.save(clientUser);
            System.out.println(">>> Usuario 'cliente123' [CLIENTE] creado <<<");
        }
        
        // --- 4. Crear Proveedor de Prueba (sin login) ---
        if (userRepository.findByIdentification("prov123").isEmpty()) {
            User supplierUser = new User();
            supplierUser.setIdentification("prov123");
            supplierUser.setNames("Proveedor De Prueba");
            supplierUser.setLastNames("S.A.S");
            supplierUser.setEmail("compras@proveedor.com");
            supplierUser.setPassword(null); // Sin contraseña
            
            supplierUser.setRols(Set.of(supplierRole)); // Asignamos el rol de Proveedor
            
            userRepository.save(supplierUser);
            System.out.println(">>> Usuario 'prov123' [PROVEEDOR] creado <<<");
        }
    }

    /**
     * Método helper para crear un Rol si no existe.
     * (NO LE PASAMOS EL ID)
     */
    private Rol createRoleIfNotExists(String name) {
        Optional<Rol> existingRole = rolRepository.findByName(name);
        if (existingRole.isEmpty()) {
            Rol newRole = new Rol();
            // ¡NO PONEMOS newRole.setId()!
            newRole.setName(name);
            return rolRepository.save(newRole);
        }
        return existingRole.get();
    }
}