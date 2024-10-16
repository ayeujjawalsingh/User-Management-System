package com.ujjawal.user_management_system.otpservice.controller;

import com.ujjawal.user_management_system.otpservice.dto.GenerateOTPRequest;
import com.ujjawal.user_management_system.otpservice.dto.GenerateOTPResponse;
import com.ujjawal.user_management_system.otpservice.service.GenerateOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/otp")
public class GenerateOTPController {

    private final GenerateOTPService generateOTPService;

    @Autowired
    public GenerateOTPController(GenerateOTPService generateOTPService) {
        this.generateOTPService = generateOTPService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerateOTPResponse> generateOTP(@Valid @RequestBody GenerateOTPRequest request) {
        try {
            GenerateOTPResponse response = generateOTPService.generateOTP(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle invalid input
            return ResponseEntity
                .badRequest()
                .body(new GenerateOTPResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenerateOTPResponse(500, "An unexpected error occurred", null));
        }
    }
}
