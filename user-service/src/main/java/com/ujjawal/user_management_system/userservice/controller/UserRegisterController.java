package com.ujjawal.user_management_system.userservice.controller;

import com.ujjawal.user_management_system.userservice.dto.UserRegisterResponse;
import com.ujjawal.user_management_system.userservice.dto.UserRegisterRequest;
import com.ujjawal.user_management_system.userservice.model.UserProfileModel;
import com.ujjawal.user_management_system.userservice.model.UserModel;
import com.ujjawal.user_management_system.userservice.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserRegisterController {

    private final UserRegisterService userService;

    @Autowired
    public UserRegisterController(UserRegisterService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> createUser(@RequestBody UserRegisterRequest request) {
        UserModel user = createUserModel(request);
        UserProfileModel userProfile = createUserProfileModel(request);
    
        // Call the service and get the response
        UserRegisterResponse response = userService.createUser(user, userProfile);
    
        // Return the appropriate HTTP status based on the response
        return switch (response.statusCode()) {
            case 201 -> ResponseEntity.status(HttpStatus.CREATED).body(response);
            case 400 -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        };
    }

    // Private methods to avoid code duplication
    private UserModel createUserModel(UserRegisterRequest request) {
        UserModel user = new UserModel();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPassword(request.getPassword());
        return user;
    }

    private UserProfileModel createUserProfileModel(UserRegisterRequest request) {
        UserProfileModel userProfile = new UserProfileModel();
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setDob(request.getDob());
        userProfile.setAddress(request.getAddress());
        userProfile.setGender(request.getGender());
        userProfile.setProfilePicture(request.getProfilePicture());
        return userProfile;
    }
}
