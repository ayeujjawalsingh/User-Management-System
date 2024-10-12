package com.ujjawal.user_management_system.userservice.exception;

public class InvalidUserInputException extends RuntimeException {
    public InvalidUserInputException(String message) {
        super(message);
    }
}
