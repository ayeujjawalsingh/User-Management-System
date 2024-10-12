package com.ujjawal.user_management_system.authservice.controller;

import com.ujjawal.user_management_system.authservice.dto.LoginRequest;
import com.ujjawal.user_management_system.authservice.dto.LoginResponse;
import com.ujjawal.user_management_system.authservice.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Login request: " + request);
            // LoginResponse response = loginService.authenticateUser(request.getIdentifier(), request.getPassword());
            LoginResponse response;
            if ("otp".equalsIgnoreCase(request.getAction() != null ? request.getAction() : "")) {
                response = loginService.authenticateUser(request.getIdentifier(), request.getOtp(), true);
            } else {
                response = loginService.authenticateUser(request.getIdentifier(), request.getPassword(), false);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: " + e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage(), ""));
        }
    }
}
