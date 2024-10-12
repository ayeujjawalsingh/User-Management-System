package com.ujjawal.user_management_system.userservice.controller;

import com.ujjawal.user_management_system.userservice.dto.UserProfileUpdateRequest;
import com.ujjawal.user_management_system.userservice.dto.UserRegisterResponse;
import com.ujjawal.user_management_system.userservice.service.UserUpdateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserUpdateProfileController {
    private final UserUpdateProfileService userUpdateProfileService;

    @Autowired
    public UserUpdateProfileController(UserUpdateProfileService userUpdateProfileService) {
        this.userUpdateProfileService = userUpdateProfileService;
    }

    @PutMapping("/{identifier}/profile")
    public ResponseEntity<UserRegisterResponse> updateUserProfile(@PathVariable String identifier, @RequestBody UserProfileUpdateRequest request) {
        try {
            UserRegisterResponse response = userUpdateProfileService.updateUserProfile(identifier, request);
            
            return switch (response.statusCode()) {
                case 200 -> ResponseEntity.ok(response);
                case 400 -> ResponseEntity.badRequest().body(response);
                case 404 -> ResponseEntity.notFound().build();
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            };
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UserRegisterResponse(400, "Invalid input: " + e.getMessage()));
        }
    }
}
