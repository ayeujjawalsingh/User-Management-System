package com.ujjawal.user_management_system.otpservice.controller;

import com.ujjawal.user_management_system.otpservice.dto.ValidateOTPRequest;
import com.ujjawal.user_management_system.otpservice.dto.ValidateOTPResponse;
import com.ujjawal.user_management_system.otpservice.service.ValidateOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/otp")
public class ValidateOTPController {

    private final ValidateOTPService validateOTPService;

    @Autowired
    public ValidateOTPController(ValidateOTPService validateOTPService) {
        this.validateOTPService = validateOTPService;
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateOTPResponse> validateOTP(@Valid @RequestBody ValidateOTPRequest request) {
        try {
            ValidateOTPResponse response = validateOTPService.validateOTP(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle invalid input
            return ResponseEntity
                .badRequest()
                .body(new ValidateOTPResponse(400, e.getMessage(), false));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ValidateOTPResponse(500, "An unexpected error occurred", false));
        }
    }
}
