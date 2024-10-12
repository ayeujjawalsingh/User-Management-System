package com.ujjawal.user_management_system.userservice.service;

import com.ujjawal.user_management_system.userservice.dto.UserProfileUpdateRequest;
import com.ujjawal.user_management_system.userservice.dto.UserRegisterResponse;
import com.ujjawal.user_management_system.userservice.exception.InvalidUserInputException;
import com.ujjawal.user_management_system.userservice.model.UserModel;
import com.ujjawal.user_management_system.userservice.model.UserProfileModel;
import com.ujjawal.user_management_system.userservice.repository.UserProfileRepository;
import com.ujjawal.user_management_system.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserUpdateProfileService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    // Constructor with dependencies injected
    @Autowired
    public UserUpdateProfileService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Update user Profile.
     */
    @Transactional
    public UserRegisterResponse updateUserProfile(String identifier, UserProfileUpdateRequest request) {
        try {
            if (request == null || isRequestEmpty(request)) {
                return new UserRegisterResponse(400, "No update data provided");
            }

            System.out.println("request: " + request);
            UserModel existingUser = findUserByIdentifier(identifier);

            validateUserProfileRequest(request);

            updateUserProfileFields(existingUser.getUserProfile(), request);
            userProfileRepository.save(existingUser.getUserProfile());

            return new UserRegisterResponse(200, "Profile updated successfully.");
        } catch (InvalidUserInputException e) {
            return new UserRegisterResponse(400, e.getMessage());
        } catch (Exception e) {
            return new UserRegisterResponse(500, "An unexpected error occurred. Please try again later.");
        }
    }

    private boolean isRequestEmpty(UserProfileUpdateRequest request) {
        return request.getFirstName() == null &&
               request.getLastName() == null &&
               request.getDob() == null &&
               request.getAddress() == null &&
               request.getGender() == null &&
               request.getProfilePicture() == null;
    }

    private UserModel findUserByIdentifier(String identifier) {
        if (identifier.matches("^\\d{10,15}$")) {
            // It's a mobile number
            return userRepository.findByMobileAndStatus(identifier, "1")
                .orElseThrow(() -> new InvalidUserInputException("User not found"));
        } else if (identifier.contains("@")) {
            // It's an email
            return userRepository.findByEmailAndStatus(identifier, "1")
                .orElseThrow(() -> new InvalidUserInputException("User not found"));
        } else {
            // It's a username
            return userRepository.findByUsernameAndStatus(identifier, "1")
                .orElseThrow(() -> new InvalidUserInputException("User not found"));
        }
    }

    private void updateUserProfileFields(UserProfileModel existingProfile, UserProfileUpdateRequest request) {
        if (request.getFirstName() != null) {
            existingProfile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            existingProfile.setLastName(request.getLastName());
        }
        if (request.getDob() != null) {
            existingProfile.setDob(request.getDob());
        }
        if (request.getAddress() != null) {
            existingProfile.setAddress(request.getAddress());
        }
        if (request.getGender() != null) {
            existingProfile.setGender(request.getGender());
        }
        if (request.getProfilePicture() != null) {
            existingProfile.setProfilePicture(request.getProfilePicture());
        }
        existingProfile.setUpdatedAt(LocalDateTime.now());
    }

    private void validateUserProfileRequest(UserProfileUpdateRequest request) {
        if (request == null) {
            throw new InvalidUserInputException("User profile update request cannot be null");
        }

        if (request.getFirstName() != null) {
            validateName(request.getFirstName(), "First name");
        }
        if (request.getLastName() != null) {
            validateName(request.getLastName(), "Last name");
        }
        if (request.getDob() != null) {
            validateDateOfBirth(request.getDob());
        }
        if (request.getAddress() != null) {
            validateAddress(request.getAddress());
        }
        if (request.getGender() != null) {
            validateGender(request.getGender());
        }
        if (request.getProfilePicture() != null) {
            validateProfilePicture(request.getProfilePicture());
        }
    }

    private void validateName(String name, String fieldName) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidUserInputException(fieldName + " is required");
        }
        if (name.length() > 50) {
            throw new InvalidUserInputException(fieldName + " cannot exceed 50 characters");
        }
    }

    private void validateDateOfBirth(LocalDate dob) {
        if (dob == null) {
            throw new InvalidUserInputException("Date of birth is required");
        }
        LocalDate now = LocalDate.now();
        if (dob.isAfter(now)) {
            throw new InvalidUserInputException("Date of birth cannot be in the future");
        }
        if (dob.isBefore(now.minusYears(150))) {
            throw new InvalidUserInputException("Date of birth cannot be more than 150 years ago");
        }
    }

    private void validateAddress(String address) {
        if (address.isEmpty()) {
            throw new InvalidUserInputException("Address cannot be empty");
        }
        if (address.length() > 200) {
            throw new InvalidUserInputException("Address cannot exceed 200 characters");
        }
    }

    private void validateGender(String gender) {
        List<String> validGenders = List.of("Male", "Female", "Other", "Prefer not to say");
        if (!validGenders.contains(gender)) {
            throw new InvalidUserInputException("Invalid gender. Allowed values are: " + String.join(", ", validGenders));
        }
    }

    private void validateProfilePicture(String profilePicture) {
        if (profilePicture.isEmpty()) {
            throw new InvalidUserInputException("Profile picture URL cannot be empty");
        }
        if (profilePicture.length() > 255) {
            throw new InvalidUserInputException("Profile picture URL cannot exceed 255 characters");
        }
    }
}
