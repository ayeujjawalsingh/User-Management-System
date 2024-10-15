package com.ujjawal.user_management_system.authservice.controller;

import com.ujjawal.user_management_system.authservice.dto.TokenRefreshRequest;
import com.ujjawal.user_management_system.authservice.dto.TokenRefreshResponse;
import com.ujjawal.user_management_system.authservice.service.RefreshTokenService;
import com.ujjawal.user_management_system.authservice.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        try {
            System.out.println("refresh token: "+request.getRefreshToken());
            String requestRefreshToken = request.getRefreshToken();
            TokenRefreshResponse response = refreshTokenService.createAccessTokenByRefreshToken(requestRefreshToken);
            return ResponseEntity.ok(response);
        } catch (TokenRefreshException e) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleTokenRefreshException(TokenRefreshException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
