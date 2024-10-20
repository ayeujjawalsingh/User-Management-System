package com.ujjawal.user_management_system.authservice.service;

import com.ujjawal.user_management_system.authservice.dto.LoginResponse;
import com.ujjawal.user_management_system.authservice.grpc.UserResponse;
//import com.ujjawal.user_management_system.authservice.auth.AuthService;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.StatusRuntimeException;
import com.ujjawal.user_management_system.authservice.grpc.UserServiceClient;
import com.ujjawal.user_management_system.authservice.model.RefreshTokenModel;
import com.ujjawal.user_management_system.authservice.repository.RefreshTokenRepository;
import java.util.UUID;
import java.time.ZoneOffset;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final Argon2 argon2;
    private final JwtService jwtService;
    @Autowired
    private UserServiceClient userServiceClient;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public LoginService(JwtService jwtService, UserServiceClient userServiceClient, RefreshTokenRepository refreshTokenRepository) {
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean verifyPassword(String input, String hashed) {
        input = input.trim();  // Trim any leading or trailing whitespace
        return argon2.verify(hashed, input.toCharArray());
    }

    public LoginResponse authenticateUser(String identifier, String credential, boolean isOtp) {
        try {
            if (identifier == null || identifier.isEmpty()) {
                return new LoginResponse(404,"Identifier is missing or empty", null, null);
            }

            System.out.println("identifier: " + identifier);
            UserResponse response = userServiceClient.getUserByIdentifier(identifier);
            System.out.println("response: " + response);
            if (! response.getUserExists()) {
                return new LoginResponse(404, "User not found", null, null);
            }
            System.out.println("Hashed Password: " + response.getHashedPassword());
            if (verifyPassword(credential, response.getHashedPassword())) {
                String accessToken = jwtService.generateToken(response.getUserId(), "accessToken");

                // Generate and save refresh token
                String refreshToken = jwtService.generateToken(response.getUserId(), "refreshToken");
                RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
                refreshTokenModel.setToken(refreshToken);
                refreshTokenModel.setUserId(UUID.fromString(response.getUserId()));
                refreshTokenModel.setExpiryDate(LocalDateTime.now().plusDays(15).toInstant(ZoneOffset.UTC));
                refreshTokenRepository.save(refreshTokenModel);

                logger.info("Login successful for user: {}", identifier);
                return new LoginResponse(200, "Login successful", accessToken, refreshToken);
            } else {
                logger.warn("Invalid password for user: {}", identifier);
                return new LoginResponse(401, "Invalid password", null, null);
            }
        } catch (StatusRuntimeException e) {
            logger.error("gRPC communication error during authentication", e);
            return new LoginResponse(500, "Authentication service unavailable: " + e.getMessage(), null, null);
        } catch (Exception e) {
            logger.error("Unexpected error during authentication", e);
            return new LoginResponse(500, "An unexpected error occurred: " + e.getMessage(), null, null);
        }
    }

//    private LoginResponse handleOtpAuthentication(UserResponse user, String otp) {
//        LocalDateTime now = LocalDateTime.now();
//        if (!StringUtils.hasText(user.getOtp())) {
//            if (user.getOtpExpiredAt() != null && user.getOtpExpiredAt().isAfter(now)) {
//                return new LoginResponse(400, "OTP has already been used", null);
//            }
//            return new LoginResponse(400, "OTP has not been generated for this user", null);
//        }
//        if (user.getOtpExpiredAt().isBefore(now)) {
//            return new LoginResponse(401, "OTP has expired", null);
//        }
//
//        if (verifyPassword(otp, user.getOtp())) {
//            String token = jwtService.generateToken(user.getUsername());
//
//            // Update user OTP status
//            authService.updateUserOtpStatus(user.getUsername(), null, null);
//
//            return new LoginResponse(200, "Login successful", token);
//        } else {
//            return new LoginResponse(401, "Invalid OTP", null);
//        }
//    }

}
