package com.papers.paperspapeleria.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {

    private String identificacion;

    private String tipoPersona;
    private String tipoIdentificacion;
    private String nombres;
    private String apellidos;
    private String ciudad;
    private String direccion;

    private String nombresContacto;
    private String apellidosContacto;
    private String email;
    private String telefono;

    private String username;
    private String password;

    private boolean active;
    private Set<String> roles;
}