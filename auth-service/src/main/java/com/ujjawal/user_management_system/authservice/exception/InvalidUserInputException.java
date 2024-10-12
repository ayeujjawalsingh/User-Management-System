package com.ujjawal.user_management_system.authservice.exception;

public class InvalidUserInputException extends RuntimeException {
    public InvalidUserInputException(String message) {
        super(message);
    }
}
