//package com.ujjawal.user_management_system.authservice.controller;
//
//import com.ujjawal.user_management_system.authservice.dto.TokenRefreshRequest;
//import com.ujjawal.user_management_system.authservice.dto.TokenRefreshResponse;
//import com.ujjawal.user_management_system.authservice.service.RefreshTokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/api/auth")
//public class RefreshTokenController {
//
//    @Autowired
//    private RefreshTokenService refreshTokenService;
//
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(refreshToken -> {
//                    String token = refreshTokenService.generateNewToken(refreshToken.getUserId());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
//    }
//}
