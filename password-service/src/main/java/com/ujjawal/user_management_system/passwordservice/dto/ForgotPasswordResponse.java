package com.ujjawal.user_management_system.passwordservice.dto;

public class ForgotPasswordResponse {
    private int statusCode;
    private String message;
    private boolean success;

    // Default constructor
    public ForgotPasswordResponse() {}

    // Constructor with parameters
    public ForgotPasswordResponse(int statusCode, String message, boolean success) {
        this.statusCode = statusCode;
        this.message = message;
        this.success = success;
    }

    // Getters and setters
    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
