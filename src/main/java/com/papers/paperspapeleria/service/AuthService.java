package com.papers.paperspapeleria.service;

import com.papers.paperspapeleria.dto.LoginRequest;
import com.papers.paperspapeleria.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}