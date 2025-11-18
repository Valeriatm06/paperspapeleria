package com.papers.paperspapeleria.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
@Table(name = "usuarios")
public class User {
    @Id
    @Column(name = "identificacion", length = 20)
    private String identification;

    @Column(name = "tipo_persona")
    private String personType;

    @Column(name = "tipo_identificacion")
    private String idType;

    @Column(nullable = false)
    private String names;

    @Column(nullable = false)
    private String lastNames;

    private String city;
    private String address;

    @Column(name = "nombres_contacto")
    private String contactName;

    @Column(name = "apellidos_contacto")
    private String contactLastName;

    @Column(unique = true)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 50, unique = true)
    private String username;

    @Column(name = "password_hash")
    private String password;

    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> rols = new HashSet<>();
}