package com.ujjawal.user_management_system.otpservice.dto;

import jakarta.validation.constraints.NotBlank;

public class GenerateOTPRequest {

    @NotBlank(message = "Identifier cannot be blank")
    private String identifier;

    // Constructor
    public GenerateOTPRequest() {
    }

    // Getter and Setter
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    // toString method for logging and debugging
    @Override
    public String toString() {
        return "GenerateOTPRequest{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}
