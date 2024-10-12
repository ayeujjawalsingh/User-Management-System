package com.ujjawal.user_management_system.authservice.dto;

public class UserResponse {
   private String hashedPassword;

   // Default constructor
   public UserResponse() {}

   // Constructor with all fields
   public UserResponse(String hashedPassword) {
       this.hashedPassword = hashedPassword;
   }

   // Getters and setters
   public String getHashedPassword() {
       return hashedPassword;
   }

   public void setHashedPassword(String hashedPassword) {
       this.hashedPassword = hashedPassword;
   }

   @Override
   public String toString() {
       return "UserResponse{" +
               "hashedPassword='" + hashedPassword + '\'' +
               '}';
   }
}