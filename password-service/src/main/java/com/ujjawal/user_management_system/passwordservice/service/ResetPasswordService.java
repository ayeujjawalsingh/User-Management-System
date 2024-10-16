package com.ujjawal.user_management_system.passwordservice.service;

import com.ujjawal.user_management_system.passwordservice.dto.ResetPasswordRequest;
import com.ujjawal.user_management_system.passwordservice.dto.ResetPasswordResponse;
import com.ujjawal.user_management_system.passwordservice.grpc.UserServiceClient;
import com.ujjawal.user_management_system.passwordservice.grpc.UserPasswordServiceClient;
import com.ujjawal.user_management_system.passwordservice.grpc.UserPasswordResponse;
import com.ujjawal.user_management_system.passwordservice.grpc.UserResponse;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private final UserServiceClient userServiceClient;
    private final UserPasswordServiceClient userPasswordServiceClient;

    @Autowired
    public ResetPasswordService(UserServiceClient userServiceClient, UserPasswordServiceClient userPasswordServiceClient) {
        this.userServiceClient = userServiceClient;
        this.userPasswordServiceClient = new UserPasswordServiceClient();
    }

    public boolean verifyPassword(String input, String hashed) {
        if (input == null || hashed == null) {
            return false; // Added a null check for safety
        }
        return ARGON2.verify(hashed, input.trim().toCharArray());
    }

    public ResetPasswordResponse processResetPassword(ResetPasswordRequest request) {
        try {
            String identifier = request.getIdentifier();
            if (identifier == null || identifier.trim().isEmpty()) {
                return new ResetPasswordResponse(HttpStatus.BAD_REQUEST.value(), "Identifier cannot be empty", false);
            }

            String oldPassword = request.getOldPassword();
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                return new ResetPasswordResponse(HttpStatus.BAD_REQUEST.value(), "Old password cannot be empty", false);
            }

            String newPassword = request.getNewPassword();
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return new ResetPasswordResponse(HttpStatus.BAD_REQUEST.value(), "New password cannot be empty", false);
            }

            if (oldPassword.equals(newPassword)) {
                return new ResetPasswordResponse(HttpStatus.BAD_REQUEST.value(), "New password must be different from the old password", false);
            }

            UserResponse userResponse = userServiceClient.getUserByIdentifier(identifier);

            // Check if user exists
            if (!userResponse.getUserExists()) {
                logger.warn("User not found for identifier: {}", identifier);
                return new ResetPasswordResponse(HttpStatus.NOT_FOUND.value(), "User not found", false);
            }

            if (!verifyPassword(oldPassword, userResponse.getHashedPassword())) {
                return new ResetPasswordResponse(HttpStatus.UNAUTHORIZED.value(), "Incorrect old password", false);
            }

            // Hash the new password with Argon2
            String hashedNewPassword = hashWithArgon2(newPassword);

            UserPasswordResponse passwordResponse = userPasswordServiceClient.setPasswordByIdentifier(identifier, hashedNewPassword);

            if (!passwordResponse.getPasswordSaved()) {
                return new ResetPasswordResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update password. Please try again", false);
            }

            return new ResetPasswordResponse(HttpStatus.OK.value(), "Password reset successfully", true);
        } catch (Exception e) {
            logger.error("Error in processResetPassword", e);
            return new ResetPasswordResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while resetting the password", false);
        }
    }

    private String hashWithArgon2(String input) {
        return ARGON2.hash(22, 65536, 4, input.toCharArray());
    }
}
