package com.papers.paperspapeleria.mapper;

import com.papers.paperspapeleria.dto.UserDTO;
import com.papers.paperspapeleria.entity.User;
import com.papers.paperspapeleria.entity.Rol; // Importar Rol
import com.papers.paperspapeleria.repository.RolRepository; // Importar RolRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityNotFoundException; // Para manejar errores si un rol no existe

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RolRepository rolRepository; // 💡 Inyectar RolRepository

    @Autowired
    public UserMapper(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Convierte una Entidad User a un UserDTO.
     * Mapea TODOS los campos del formulario de frontend.
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

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
        // dto.setPassword(null); // Nunca mapear la contraseña a DTO por seguridad
        dto.setActive(user.isActive());
        
        // Convertir el Set<Rol> a un Set<String> de nombres de rol
        dto.setRoles(user.getRols().stream()
                .map(Rol::getName)
                .collect(Collectors.toSet()));

        return dto;
    }

    /**
     * Convierte un UserDTO a una Entidad User.
     * Esto es usado para crear y actualizar usuarios.
     * NO se mapea la contraseña aquí directamente, se gestiona en el servicio.
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setIdentification(dto.getIdentificacion());
        user.setPersonType(dto.getTipoPersona());
        user.setIdType(dto.getTipoIdentificacion());
        user.setNames(dto.getNombres());
        user.setLastNames(dto.getApellidos());
        user.setCity(dto.getCiudad());
        user.setAddress(dto.getDireccion());
        user.setContactName(dto.getNombresContacto());
        user.setContactLastName(dto.getApellidosContacto());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getTelefono());
        user.setUsername(dto.getUsername());
        // La contraseña se gestiona en el servicio (encriptación)
        user.setActive(dto.isActive());

        // Mapear los roles del DTO (Set<String>) a Set<Rol>
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Rol> rols = new HashSet<>();
            for (String roleName : dto.getRoles()) {
                // Buscar cada rol por su nombre en la base de datos
                Rol rol = rolRepository.findByName(roleName)
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleName));
                rols.add(rol);
            }
            user.setRols(rols);
        } else {
            user.setRols(new HashSet<>()); // Si no hay roles, un Set vacío
        }

        return user;
    }

    /**
     * Actualiza los campos de una entidad User existente a partir de un UserDTO.
     * Esto es crucial para actualizaciones parciales y no sobrescribir la contraseña si no se cambia.
     */
    public void updateEntityFromDTO(UserDTO dto, User user) {
        if (dto == null || user == null) {
            return;
        }

        // No actualizar la identificación en una actualización (es la clave primaria)
        
        // Solo actualizar si el DTO proporciona un nuevo valor (o si queremos que lo haga)
        if (dto.getTipoPersona() != null) user.setPersonType(dto.getTipoPersona());
        if (dto.getTipoIdentificacion() != null) user.setIdType(dto.getTipoIdentificacion());
        if (dto.getNombres() != null) user.setNames(dto.getNombres());
        if (dto.getApellidos() != null) user.setLastNames(dto.getApellidos());
        if (dto.getCiudad() != null) user.setCity(dto.getCiudad());
        if (dto.getDireccion() != null) user.setAddress(dto.getDireccion());
        if (dto.getNombresContacto() != null) user.setContactName(dto.getNombresContacto());
        if (dto.getApellidosContacto() != null) user.setContactLastName(dto.getApellidosContacto());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getTelefono() != null) user.setPhoneNumber(dto.getTelefono());
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        // La contraseña se gestiona en el servicio si se proporciona en el DTO

        user.setActive(dto.isActive()); // Siempre actualizar el estado 'active'

        // Actualizar los roles
        if (dto.getRoles() != null) {
            Set<Rol> newRols = new HashSet<>();
            for (String roleName : dto.getRoles()) {
                Rol rol = rolRepository.findByName(roleName)
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleName));
                newRols.add(rol);
            }
            user.setRols(newRols);
        }
    }
}