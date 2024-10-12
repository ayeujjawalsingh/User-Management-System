//package com.ujjawal.user_management_system.authservice.auth;
//
//import com.ujjawal.user_management_system.userservice.grpc.PasswordRequest;
//import com.ujjawal.user_management_system.userservice.grpc.PasswordResponse;
//import com.ujjawal.user_management_system.userservice.grpc.UserServiceGrpc;
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.client.inject.GrpcClient;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//
//    @GrpcClient("user-service")
//    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;
//
//    public String getHashedPassword(String identifier) {
//        // Create a request for gRPC communication with user-service
//        PasswordRequest request = PasswordRequest.newBuilder()
//                .setIdentifier(identifier)
//                .build();
//
//        // Get the response from user-service (fetch the hashed password)
//        PasswordResponse response = userServiceStub.getHashedPassword(request);
//
//        // Return the hashed password
//        return response.getHashedPassword();
//    }
//
////    // Method to verify the password (could use BCrypt or any password hashing library)
////    public boolean verifyPassword(String plainPassword, String hashedPassword) {
////        // Example with BCrypt
////        return BCrypt.checkpw(plainPassword, hashedPassword);
////    }
//}
