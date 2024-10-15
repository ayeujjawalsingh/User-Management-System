package com.ujjawal.user_management_system.authservice.dto;

public class LoginResponse {
    private int statusCode;
    private String message;
    private String accessToken;
    private String refreshToken;

    // Constructor, getters, and setters
    public LoginResponse(int statusCode, String message,  String accessToken, String refreshToken) {
        this.statusCode = statusCode;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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

    public String getaccessToken() {
        return accessToken;
    }

    public void setaccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}