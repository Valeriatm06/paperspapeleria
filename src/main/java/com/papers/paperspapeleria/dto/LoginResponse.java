package com.papers.paperspapeleria.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String authToken;
    private String username;

    public LoginResponse(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
}