package com.musicbooking.dto;

import java.util.List;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles; // CHANGED from String role

    public AuthResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    // Getters
    public String getToken() { return token; }
    public String getType() { return type; }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; } // CHANGED from getRole()
}