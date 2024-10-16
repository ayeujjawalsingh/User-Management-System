package com.ujjawal.user_management_system.passwordservice.dto;

public class ResetPasswordRequest {
    private String identifier;
    private String oldPassword;
    private String newPassword;

    // Default constructor
    public ResetPasswordRequest() {}

    // Constructor with parameters
    public ResetPasswordRequest(String identifier, String oldPassword, String newPassword) {
        this.identifier = identifier;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Getters and setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
