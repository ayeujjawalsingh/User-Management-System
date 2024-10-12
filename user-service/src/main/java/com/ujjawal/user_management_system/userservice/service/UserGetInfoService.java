package com.ujjawal.user_management_system.userservice.service;

import com.ujjawal.user_management_system.userservice.dto.UserDetailsResponse;
import com.ujjawal.user_management_system.userservice.repository.UserRepository;
import com.ujjawal.user_management_system.userservice.model.UserModel;
import com.ujjawal.user_management_system.userservice.model.UserProfileModel;
import com.ujjawal.user_management_system.userservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserGetInfoService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserGetInfoService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public UserDetailsResponse getUserProfile(String identifier) {
        try {
            Optional<UserModel> userOptional = findUserByIdentifier(identifier);
            System.out.println(userOptional.isPresent());
            System.out.println(userOptional.isEmpty());
            if (userOptional.isEmpty()) {
                return UserDetailsResponse.notFound("User not found for identifier: " + identifier);
            }

            UserModel user = userOptional.get();
            Optional<UserProfileModel> userProfileOptional = userProfileRepository.findActiveByUserId(user.getId());

            if (userProfileOptional.isEmpty()) {
                return UserDetailsResponse.notFound("User profile not found for user: " + user.getUsername());
            }

            UserProfileModel userProfile = userProfileOptional.get();
            UserDetailsResponse.UserDetails responseDetails = new UserDetailsResponse.UserDetails(
                    user.getUsername(),
                    userProfile.getFirstName(),
                    userProfile.getLastName(),
                    user.getEmail(),
                    user.getMobile(),
                    userProfile.getAddress(),
                    userProfile.getGender(),
                    userProfile.getProfilePicture(),
                    userProfile.getDob()
            );
            return UserDetailsResponse.success(responseDetails);
        } catch (RuntimeException e) {
            System.out.println("Runtime Exception: "+ e.getMessage());
            return UserDetailsResponse.notFound("Something happened; Please try again!!!");
        } catch (Exception e) {
            return UserDetailsResponse.error("An error occurred while retrieving the user profile");
        }
    }

    /**
     * Finds a user by identifier, which could be a username, mobile number, or email.
     */
    private Optional<UserModel> findUserByIdentifier(String identifier) {
        if (identifier.matches("^\\d{10,15}$")) {
            // It's a mobile number
            return userRepository.findByMobileAndStatus(identifier, "1");
        } else if (identifier.contains("@")) {
            // It's an email
            return userRepository.findByEmailAndStatus(identifier, "1");
        } else {
            // It's a username
            return userRepository.findByUsernameAndStatus(identifier, "1");
        }
    }
}
