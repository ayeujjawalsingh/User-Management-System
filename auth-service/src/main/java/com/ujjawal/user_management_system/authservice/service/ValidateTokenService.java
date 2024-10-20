package com.ujjawal.user_management_system.authservice.service;

import com.ujjawal.user_management_system.authservice.dto.ValidateTokenResponse;
import com.ujjawal.user_management_system.authservice.grpc.UserIdServiceClient;
import com.ujjawal.user_management_system.authservice.grpc.UserIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ValidateTokenService {

    private final JwtService jwtService;
    @Autowired
    private UserIdServiceClient userIdServiceClient;

    public ValidateTokenService(JwtService jwtService, UserIdServiceClient userIdServiceClient) {
        this.jwtService = jwtService;
        this.userIdServiceClient = userIdServiceClient;
    }

    public ResponseEntity<ValidateTokenResponse> validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ValidateTokenResponse(null, false));
        }

        try {
            token = token.split(" ")[1];

            Map<String, Object> validateTokenResponse = jwtService.validateToken(token);

            UserIdResponse response = userIdServiceClient.validateUserByUserId((String) validateTokenResponse.get("userId"));

            if (! response.getUserExists()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ValidateTokenResponse(null, false));
            }

            if ((Boolean) validateTokenResponse.get("isValid")) {
                return ResponseEntity.ok(new ValidateTokenResponse((String) validateTokenResponse.get("userId"), true));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ValidateTokenResponse(null, false));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ValidateTokenResponse(null, false));
        }
    }
}
