package com.ujjawal.user_management_system.otpservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import com.ujjawal.user_management_system.otpservice.grpc.UserRequest;
import com.ujjawal.user_management_system.otpservice.grpc.UserResponse;
import com.ujjawal.user_management_system.otpservice.grpc.UserServiceGrpc;

@Service
public class UserServiceClient {

    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public UserServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse getUserByIdentifier(String identifier) {
        UserRequest request = UserRequest.newBuilder().setIdentifier(identifier).build();
        System.out.println("Reached Here...........");
        System.out.println(userServiceBlockingStub);
        return userServiceBlockingStub.getUserByIdentifier(request);
    }
}