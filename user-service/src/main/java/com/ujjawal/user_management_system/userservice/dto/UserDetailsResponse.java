package com.ujjawal.user_management_system.userservice.dto;

import java.time.LocalDate;

public record UserDetailsResponse(
        int statusCode,
        String message,
        UserDetails userDetails
) {
    public record UserDetails(
            String username,
            String firstName,
            String lastName,
            String email,
            String mobile,
            String address,
            String gender,
            String profilePicture,
            LocalDate dob
    ) {}

    public static UserDetailsResponse success(UserDetails userDetails) {
        return new UserDetailsResponse(200, "User profile retrieved successfully", userDetails);
    }

    public static UserDetailsResponse notFound(String message) {
        return new UserDetailsResponse(404, message, null);
    }

    public static UserDetailsResponse error(String message) {
        return new UserDetailsResponse(500, message, null);
    }
}
