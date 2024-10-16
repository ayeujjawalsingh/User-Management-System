package com.ujjawal.user_management_system.otpservice.service;

import com.ujjawal.user_management_system.otpservice.dto.GenerateOTPRequest;
import com.ujjawal.user_management_system.otpservice.dto.GenerateOTPResponse;
import com.ujjawal.user_management_system.otpservice.grpc.UserResponse;
import com.ujjawal.user_management_system.otpservice.model.UserOTPModel;
import com.ujjawal.user_management_system.otpservice.repository.UserOTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ujjawal.user_management_system.otpservice.grpc.UserServiceClient;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class GenerateOTPService {
    private static final Logger logger = LoggerFactory.getLogger(GenerateOTPService.class);
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    private final UserOTPRepository userOTPRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public GenerateOTPService(UserOTPRepository userOTPRepository, UserServiceClient userServiceClient) {
        this.userOTPRepository = userOTPRepository;
        this.userServiceClient = userServiceClient;
    }

    public GenerateOTPResponse generateOTP(GenerateOTPRequest request) {
        try {
            String identifier = request.getIdentifier();
            if (identifier == null || identifier.trim().isEmpty()) {
                throw new IllegalArgumentException("Identifier cannot be empty");
            }

            UserResponse response = userServiceClient.getUserByIdentifier(identifier);

            logger.info("User service response: {}", response);
            if (!response.getUserExists()) {
                return new GenerateOTPResponse(404, "User not found", null);
            }

            UUID userId = UUID.fromString(response.getUserId());

            // Check for active OTPs and expire them
            List<UserOTPModel> activeOTPs = userOTPRepository.findActiveOTPsByUserId(userId);
            if (!activeOTPs.isEmpty()) {
                for (UserOTPModel activeOTP : activeOTPs) {
                    activeOTP.setStatus(UserOTPModel.OTPStatus.EXPIRED);
                    activeOTP.setExpirationTime(Instant.now());
                }
                userOTPRepository.saveAll(activeOTPs);
                logger.info("Expired {} active OTPs for user {}", activeOTPs.size(), userId);
            }

            String otp = generateRandomOTP();

            // Generate and save new OTP
            UserOTPModel userOTPModel = new UserOTPModel();
            userOTPModel.setUserId(userId);
            userOTPModel.setOtp(hashWithArgon2(otp));
            userOTPModel.setExpirationTime(Instant.now().plus(5, ChronoUnit.MINUTES));
            userOTPModel.setStatus(UserOTPModel.OTPStatus.ACTIVE);
            userOTPRepository.save(userOTPModel);

            return new GenerateOTPResponse(200, "OTP generated successfully", otp);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            return new GenerateOTPResponse(400, e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error generating OTP", e);
            return new GenerateOTPResponse(500, "An error occurred while generating OTP", null);
        }
    }

    private String generateRandomOTP() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    private String hashWithArgon2(String input) {
        return ARGON2.hash(22, 65536, 4, input.toCharArray());
    }
}
