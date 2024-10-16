package com.ujjawal.user_management_system.passwordservice.dto;

public class ForgotPasswordRequest {
    private String identifier;
    private String otp;
    private String password;

    // Default constructor
    public ForgotPasswordRequest() {}

    // Constructor with parameters
    public ForgotPasswordRequest(String identifier, String otp, String password) {
        this.identifier = identifier;
        this.otp = otp;
        this.password = password;
    }

    // Getters and setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
