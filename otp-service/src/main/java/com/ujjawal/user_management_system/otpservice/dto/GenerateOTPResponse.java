package com.ujjawal.user_management_system.otpservice.dto;

public class GenerateOTPResponse {

    private int statusCode;
    private String message;
    private String otp;

    // Default constructor
    public GenerateOTPResponse() {
    }

    // Parameterized constructor
    public GenerateOTPResponse(int statusCode, String message, String otp) {
        this.statusCode = statusCode;
        this.message = message;
        this.otp = otp;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    // toString method for logging and debugging
    @Override
    public String toString() {
        return "GenerateOTPResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
