package com.ujjawal.user_management_system.passwordservice.controller;

import com.ujjawal.user_management_system.passwordservice.dto.*;
import com.ujjawal.user_management_system.passwordservice.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/reset")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            ResetPasswordResponse response = resetPasswordService.processResetPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResetPasswordResponse errorResponse = new ResetPasswordResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
