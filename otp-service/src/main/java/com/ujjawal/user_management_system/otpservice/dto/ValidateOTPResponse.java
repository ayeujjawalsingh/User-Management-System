package com.ujjawal.user_management_system.otpservice.dto;

public class ValidateOTPResponse {

    private int statusCode;
    private String message;
    private boolean isValid;

    // Default constructor
    public ValidateOTPResponse() {
    }

    // Parameterized constructor
    public ValidateOTPResponse(int statusCode, String message, boolean isValid) {
        this.statusCode = statusCode;
        this.message = message;
        this.isValid = isValid;
    }

    // Getters and setters
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

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    // toString method for logging and debugging
    @Override
    public String toString() {
        return "ValidateOTPResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", isValid=" + isValid +
                '}';
    }
}
