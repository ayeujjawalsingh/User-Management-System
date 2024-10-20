package com.ujjawal.user_management_system.authservice.controller;

import com.ujjawal.user_management_system.authservice.dto.ValidateTokenResponse;
import com.ujjawal.user_management_system.authservice.service.ValidateTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ValidateTokenController {

    private final ValidateTokenService validateTokenService;

    @Autowired
    public ValidateTokenController(ValidateTokenService validateTokenService) {
        this.validateTokenService = validateTokenService;
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestHeader("Authorization") String token) {
        try{
            return validateTokenService.validateToken(token);
        }
        catch(Exception e){
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ValidateTokenResponse(null, false));
        }
    }
}
