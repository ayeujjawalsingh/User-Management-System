package com.ujjawal.user_management_system.authservice.grpc;

import com.ujjawal.user_management_system.authservice.grpc.UserRequest;
import com.ujjawal.user_management_system.authservice.grpc.UserResponse;
import com.ujjawal.user_management_system.authservice.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

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
