package com.ujjawal.user_management_system.passwordservice.service;

import com.ujjawal.user_management_system.passwordservice.dto.ForgotPasswordRequest;
import com.ujjawal.user_management_system.passwordservice.dto.ForgotPasswordResponse;
import com.ujjawal.user_management_system.passwordservice.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.util.Random;

@Service
public class ForgotPasswordService {
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private final UserServiceClient userServiceClient;
    private final UserPasswordServiceClient userPasswordServiceClient;
    private final UserOTPServiceClient userOTPServiceClient;

    @Autowired
    public ForgotPasswordService(UserServiceClient userServiceClient, UserPasswordServiceClient userPasswordServiceClient, UserOTPServiceClient userOTPServiceClient) {
        this.userServiceClient = userServiceClient;
        this.userPasswordServiceClient = userPasswordServiceClient;
        this.userOTPServiceClient = userOTPServiceClient;
    }

    public boolean verifyOtp(String input, String hashed) {
        if (input == null || hashed == null) {
            return false; // Added a null check for safety
        }
        return ARGON2.verify(hashed, input.trim().toCharArray());
    }

    public ForgotPasswordResponse processForgotPassword(ForgotPasswordRequest request) {
        try {
            String identifier = request.getIdentifier();
            if (identifier == null || identifier.trim().isEmpty()) {
                return new ForgotPasswordResponse(HttpStatus.BAD_REQUEST.value(), "Identifier cannot be empty", false);
            }

            String password = request.getPassword();
            if (password == null || password.trim().isEmpty()) {
                return new ForgotPasswordResponse(HttpStatus.BAD_REQUEST.value(), "Password cannot be empty", false);
            }

            String otp = request.getOtp();
            if (otp == null || otp.trim().isEmpty()) {
                return new ForgotPasswordResponse(HttpStatus.BAD_REQUEST.value(), "OTP cannot be empty", false);
            }

            UserResponse response = userServiceClient.getUserByIdentifier(identifier);

            // Check if user exists
            if (!response.getUserExists()) {
                logger.warn("User not found for identifier: {}", identifier);
                return new ForgotPasswordResponse(HttpStatus.NOT_FOUND.value(), "User not found", false);
            }

            String hashedPassword = response.getHashedPassword();

            UserOTPResponse otpResponse = userOTPServiceClient.verifyOTPByUserID(response.getUserId(), otp);

            if (!otpResponse.getOtpVerified()) {
                return new ForgotPasswordResponse(HttpStatus.BAD_REQUEST.value(), "Invalid OTP", false);
            }

            if(verifyOtp(password, hashedPassword)) {
                return new ForgotPasswordResponse(HttpStatus.BAD_REQUEST.value(), "New password must be different from the old password", false);
            }

            // Hash the new password with Argon2
            String hashedNewPassword = hashWithArgon2(password);

            UserPasswordResponse passwordResponse = userPasswordServiceClient.setPasswordByIdentifier(identifier, hashedNewPassword);

            if (!passwordResponse.getPasswordSaved()) {
                return new ForgotPasswordResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update password. Please try again", false);
            }

            return new ForgotPasswordResponse(HttpStatus.OK.value(), "Password saved successfully", true);
        } catch (Exception e) {
            logger.error("Error in processForgotPassword", e);
            return new ForgotPasswordResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing your request", false);
        }
    }

    private String hashWithArgon2(String input) {
        return ARGON2.hash(22, 65536, 4, input.toCharArray());
    }
}
