package com.ujjawal.user_management_system.authservice.service;

import com.ujjawal.user_management_system.authservice.dto.TokenRefreshResponse;
import com.ujjawal.user_management_system.authservice.exception.TokenRefreshException;
import com.ujjawal.user_management_system.authservice.model.RefreshTokenModel;
import com.ujjawal.user_management_system.authservice.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RefreshTokenService {

   @Autowired
   private JwtService jwtService;

   @Autowired
   private RefreshTokenRepository refreshTokenRepository;

   public TokenRefreshResponse createAccessTokenByRefreshToken(String refreshToken) {
       System.out.println(refreshToken);
       // Check if refresh token exists in database
        RefreshTokenModel storedToken = refreshTokenRepository.findByToken(refreshToken)
           .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token invalid"));
       System.out.println(storedToken);

       // Validate the token format and expiration
       Map<String, Object> refreshTokenStatus = jwtService.validateToken(refreshToken);
       System.out.println(refreshTokenStatus);
       // Check if the token is valid
       if (!(Boolean) refreshTokenStatus.get("isValid")) {
           throw new TokenRefreshException(refreshToken, "Invalid refresh token");
       }
       
       // Get userId from the token
       String userId = (String) refreshTokenStatus.get("userId");
       
       if (!storedToken.getUserId().toString().equals(userId)) {
           throw new TokenRefreshException(refreshToken, "Refresh token is invalid");
       }
       
       // Generate new access token
       String newAccessToken = jwtService.generateToken(userId, "accessToken");
       return new TokenRefreshResponse(newAccessToken, refreshToken);
   }
}
