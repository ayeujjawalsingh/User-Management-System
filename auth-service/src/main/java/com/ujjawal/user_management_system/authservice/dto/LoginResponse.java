package com.ujjawal.user_management_system.authservice.dto;

public class LoginResponse {
    private int statusCode;
    private String message;
    private String token;

    // Constructor, getters, and setters
    public LoginResponse(int statusCode, String message,  String token) {
        this.statusCode = statusCode;
        this.message = message;
        this.token = token;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}