package com.ujjawal.user_management_system.authservice.grpc;

import com.ujjawal.user_management_system.authservice.grpc.UserIdRequest;
import com.ujjawal.user_management_system.authservice.grpc.UserIdResponse;
import com.ujjawal.user_management_system.authservice.grpc.UserIdServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserIdServiceClient {

    private final UserIdServiceGrpc.UserIdServiceBlockingStub userIdServiceBlockingStub;

    public UserIdServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        userIdServiceBlockingStub = UserIdServiceGrpc.newBlockingStub(channel);
    }

    public UserIdResponse validateUserByUserId(String userId) {
        UserIdRequest request = UserIdRequest.newBuilder().setUserId(userId).build();
        System.out.println("Reached Here...........");
        return userIdServiceBlockingStub.validateUserByUserId(request);
    }
}