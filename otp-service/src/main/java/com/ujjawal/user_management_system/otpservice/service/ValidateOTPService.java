package com.ujjawal.user_management_system.otpservice.service;

import com.ujjawal.user_management_system.otpservice.dto.ValidateOTPRequest;
import com.ujjawal.user_management_system.otpservice.dto.ValidateOTPResponse;
import com.ujjawal.user_management_system.otpservice.grpc.UserServiceClient;
import com.ujjawal.user_management_system.otpservice.model.UserOTPModel;
import com.ujjawal.user_management_system.otpservice.repository.UserOTPRepository;
import com.ujjawal.user_management_system.otpservice.grpc.UserResponse;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ValidateOTPService {
    private static final Logger logger = LoggerFactory.getLogger(ValidateOTPService.class);

    private final Argon2 argon2;
    private final UserOTPRepository userOTPRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public ValidateOTPService(UserOTPRepository userOTPRepository, UserServiceClient userServiceClient) {
        this.userOTPRepository = userOTPRepository;
        this.userServiceClient = userServiceClient;
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    /**
     * Verifies the provided OTP against the hashed OTP stored in the database.
     *
     * @param input  The OTP input provided by the user.
     * @param hashed The hashed OTP stored in the system.
     * @return True if the OTP matches, false otherwise.
     */
    public boolean verifyOTP(String input, String hashed) {
        input = input.trim();  // Trim any leading or trailing whitespace
        return argon2.verify(hashed, input.toCharArray());
    }

    /**
     * Validates the OTP for a given request.
     *
     * @param request The OTP validation request containing the identifier and OTP.
     * @return A response indicating the result of the OTP validation.
     */
    public ValidateOTPResponse validateOTP(ValidateOTPRequest request) {
        try {
            String identifier = request.getIdentifier();

            // Validate identifier
            if (identifier == null || identifier.trim().isEmpty()) {
                logger.error("Validation failed: Identifier cannot be empty");
                throw new IllegalArgumentException("Identifier cannot be empty");
            }

            logger.info("Validating OTP for identifier: {}", identifier);

            // Fetch user information via gRPC client
            UserResponse response = userServiceClient.getUserByIdentifier(identifier);

            logger.info("User service response - User exists: {}, User ID: {}",
                    response.getUserExists(), response.getUserId());

            // Check if user exists
            if (!response.getUserExists()) {
                logger.warn("User not found for identifier: {}", identifier);
                return new ValidateOTPResponse(404, "User not found", false);
            }

            String otp = request.getOtp();

            // Validate OTP input
            if (otp == null || otp.trim().isEmpty()) {
                logger.error("Validation failed: OTP cannot be empty");
                throw new IllegalArgumentException("OTP cannot be empty");
            }

            logger.info("Attempting to validate OTP: {}", otp);

            // Find active OTP for the user
            Optional<UserOTPModel> otpModelOptional = userOTPRepository.findByUserIdAndStatus(
                    UUID.fromString(response.getUserId()), UserOTPModel.OTPStatus.ACTIVE);

            if (otpModelOptional.isPresent()) {
                UserOTPModel otpModel = otpModelOptional.get();

                logger.info("OTP found for user: {}, OTP: {}", response.getUserId(), otpModel.getOtp());

                // Verify OTP
                if(verifyOTP(otp, otpModel.getOtp())) {
                    logger.info("OTP verified");
                    if (Instant.now().isBefore(otpModel.getExpirationTime())) {
                        // OTP is valid
                        logger.info("OTP not expired");
                        otpModel.setStatus(UserOTPModel.OTPStatus.REVOKED);
                        otpModel.setOtp("");  // Instead of setting OTP to null, set it to an empty string or keep the hash.
                        userOTPRepository.save(otpModel);
                        logger.info("OTP verified and saved");
                        return new ValidateOTPResponse(200, "OTP validated successfully", true);
                    } else {
                        // OTP has expired
                        logger.info("OTP expired");
                        otpModel.setStatus(UserOTPModel.OTPStatus.EXPIRED);
                        otpModel.setOtp("");  // Set to an empty string or keep the existing value
                        userOTPRepository.save(otpModel);
                        return new ValidateOTPResponse(400, "OTP has expired", false);
                    }
                } else {
                    return new ValidateOTPResponse(400, "Invalid OTP", false);
                }
            } else {
                // OTP not found or already used
                logger.warn("No active OTP found for user: {}", response.getUserId());
                return new ValidateOTPResponse(400, "Invalid OTP", false);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation failed due to invalid input: {}", e.getMessage());
            return new ValidateOTPResponse(400, e.getMessage(), false);
        } catch (Exception e) {
            logger.error("Error occurred while validating OTP", e);
            return new ValidateOTPResponse(500, "An error occurred while validating OTP", false);
        }
    }
}
