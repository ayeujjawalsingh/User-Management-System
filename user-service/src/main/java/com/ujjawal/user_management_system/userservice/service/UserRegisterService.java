package com.ujjawal.user_management_system.userservice.service;

import com.ujjawal.user_management_system.userservice.exception.InvalidUserInputException;
import com.ujjawal.user_management_system.userservice.model.UserModel;
import com.ujjawal.user_management_system.userservice.model.UserProfileModel;
import com.ujjawal.user_management_system.userservice.dto.UserRegisterResponse;
import com.ujjawal.user_management_system.userservice.repository.UserRepository;
import com.ujjawal.user_management_system.userservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@Service
public class UserRegisterService {
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    // Constructor with dependencies injected
    @Autowired
    public UserRegisterService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Creates a new user and user Profile.
     */
    @Transactional
    public UserRegisterResponse createUser(UserModel user, UserProfileModel userProfile) {
        try {
            validateUser(user, userProfile);
            checkDuplicateUserData(user);

            hashSensitiveData(user);
            setDefaultValues(user, userProfile);

            UserModel createdUser = userRepository.save(user);
            saveUserProfile(userProfile, createdUser);

            return new UserRegisterResponse(201, "Registration successful. Please verify your email and mobile.");
        } catch (InvalidUserInputException e) {
            return new UserRegisterResponse(400, e.getMessage());
        } catch (Exception e) {
            return new UserRegisterResponse(500, "An unexpected error occurred. Please try again later.");
        }
    }

    private void checkDuplicateUserData(UserModel user) {
        if (userRepository.existsByUsernameAndStatus(user.getUsername(), "1")) {
            throw new InvalidUserInputException("Username already exists");
        }
        if (userRepository.existsByEmailAndStatus(user.getEmail(), "1")) {
            throw new InvalidUserInputException("Email already exists");
        }
        if (userRepository.existsByMobileAndStatus(user.getMobile(), "1")) {
            throw new InvalidUserInputException("Mobile number already exists");
        }
    }

    private void hashSensitiveData(UserModel user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(hashWithArgon2(user.getPassword()));
        }
    }

    private void setDefaultValues(UserModel user, UserProfileModel userProfile) {
        user.setStatus("1");
        user.setCreatedAt(LocalDateTime.now());
        userProfile.setStatus("1");
        userProfile.setCreatedAt(LocalDateTime.now());
    }

    private void saveUserProfile(UserProfileModel userProfile, UserModel createdUser) {
        userProfile.setUser(createdUser);
        userProfileRepository.save(userProfile);
    }

    private void validateUser(UserModel user, UserProfileModel userProfile) {
        if (user == null) {
            throw new InvalidUserInputException("User cannot be null");
        }

        validateName(userProfile.getFirstName(), "First name");
        validateName(userProfile.getLastName(), "Last name");
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validateMobile(user.getMobile());
        validatePassword(user.getPassword());
        validateDateOfBirth(userProfile.getDob());
    }

    private void validateName(String name, String fieldName) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidUserInputException(fieldName + " is required");
        }
        if (name.length() > 50) {
            throw new InvalidUserInputException(fieldName + " cannot exceed 50 characters");
        }
    }

    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new InvalidUserInputException("Username is required");
        }
        if (username.length() > 50) {
            throw new InvalidUserInputException("Username cannot exceed 50 characters");
        }
    }

    private void validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new InvalidUserInputException("Email is required");
        }
        if (!isValidEmail(email)) {
            throw new InvalidUserInputException("Invalid email format");
        }
    }

    private void validateMobile(String mobile) {
        if (!StringUtils.hasText(mobile)) {
            throw new InvalidUserInputException("Mobile number is required");
        }
        if (!isValidMobileNumber(mobile)) {
            throw new InvalidUserInputException("Invalid mobile number format");
        }
    }

    private void validatePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new InvalidUserInputException("Password is required");
        }
        if (password.length() < 8) {
            throw new InvalidUserInputException("Password must be at least 8 characters long");
        }
        String passwordValidationError = getPasswordValidationError(password);
        if (passwordValidationError != null) {
            throw new InvalidUserInputException(passwordValidationError);
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

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]+([._-]*[a-zA-Z0-9])*@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
    }

    private boolean isValidMobileNumber(String mobile) {
        return mobile.matches("^\\d{10,15}$");
    }

    private String getPasswordValidationError(String password) {
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            return "Password must contain at least one special character (@#$%^&+=!)";
        }
        return null;
    }

    private String hashWithArgon2(String input) {
        return ARGON2.hash(22, 65536, 4, input.toCharArray());
    }

}