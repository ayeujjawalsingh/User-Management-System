package com.ujjawal.user_management_system.otpservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ValidateOTPRequest {

    @NotBlank(message = "Identifier cannot be blank")
    private String identifier;

    @NotBlank(message = "OTP cannot be blank")
    private String otp;

    // Default constructor
    public ValidateOTPRequest() {
    }

    // Parameterized constructor
    public ValidateOTPRequest(String identifier, String otp) {
        this.identifier = identifier;
        this.otp = otp;
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

    // toString method for logging and debugging
    @Override
    public String toString() {
        return "ValidateOTPRequest{" +
                "identifier='" + identifier + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
