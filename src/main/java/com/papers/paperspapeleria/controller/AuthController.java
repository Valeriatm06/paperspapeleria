package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.dto.LoginRequest;
import com.papers.paperspapeleria.dto.LoginResponse;
import com.papers.paperspapeleria.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Delegamos al servicio (que está mockeado en el test)
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response); // Devuelve 200 OK
    }
}