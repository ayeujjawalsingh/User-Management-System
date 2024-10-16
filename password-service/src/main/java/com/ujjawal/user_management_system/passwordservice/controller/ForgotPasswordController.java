package com.ujjawal.user_management_system.passwordservice.controller;

import com.ujjawal.user_management_system.passwordservice.dto.ForgotPasswordRequest;
import com.ujjawal.user_management_system.passwordservice.dto.ForgotPasswordResponse;
import com.ujjawal.user_management_system.passwordservice.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @Autowired
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/forgot")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            ForgotPasswordResponse response = forgotPasswordService.processForgotPassword(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ForgotPasswordResponse errorResponse = new ForgotPasswordResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), false);
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ForgotPasswordResponse errorResponse = new ForgotPasswordResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing your request", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
