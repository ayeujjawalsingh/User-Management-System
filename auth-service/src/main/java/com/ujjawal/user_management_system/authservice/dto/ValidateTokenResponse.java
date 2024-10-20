package com.ujjawal.user_management_system.authservice.dto;

public class ValidateTokenResponse {
    private String userId;
    private boolean validUser;

    // Default constructor
    public ValidateTokenResponse() {}

    // Constructor with parameters
    public ValidateTokenResponse(String userId, boolean validUser) {
        this.userId = userId;
        this.validUser = validUser;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isValidUser() {
        return validUser;
    }

    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }
}
