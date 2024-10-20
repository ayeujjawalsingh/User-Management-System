package com.ujjawal.user_management_system.authservice.dto;

public class ValidateTokenRequest {
    private String token;

    // Default constructor
    public ValidateTokenRequest() {}

    // Constructor with parameter
    public ValidateTokenRequest(String token) {
        this.token = token;
    }

    // Getter and setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
