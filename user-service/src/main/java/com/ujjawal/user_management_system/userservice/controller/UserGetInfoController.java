package com.ujjawal.user_management_system.userservice.controller;

import com.ujjawal.user_management_system.userservice.dto.UserDetailsResponse;
import com.ujjawal.user_management_system.userservice.service.UserGetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserGetInfoController {
    private final UserGetInfoService userGetInfoService;

    @Autowired
    public UserGetInfoController(UserGetInfoService userGetInfoService) {
        this.userGetInfoService = userGetInfoService;
    }

    @GetMapping("/{identifier}/profile")
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable String identifier) {
        UserDetailsResponse response = userGetInfoService.getUserProfile(identifier);

        return switch (response.statusCode()) {
            case 200 -> ResponseEntity.ok(response);
            case 404 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            case 500 -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            default -> ResponseEntity.status(response.statusCode()).body(response);
        };
    }
}
