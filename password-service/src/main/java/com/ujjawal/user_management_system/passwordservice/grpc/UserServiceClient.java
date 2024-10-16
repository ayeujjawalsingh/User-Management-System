package com.ujjawal.user_management_system.passwordservice.grpc;

import com.ujjawal.user_management_system.passwordservice.grpc.UserRequest;
import com.ujjawal.user_management_system.passwordservice.grpc.UserResponse;
import com.ujjawal.user_management_system.passwordservice.grpc.UserServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class UserServiceClient {

    private final ManagedChannel channel;
    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public UserServiceClient() {
        // Initialize gRPC channel and stubs
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse getUserByIdentifier(String identifier) {
        // Build the gRPC request
        UserRequest request = UserRequest.newBuilder().setIdentifier(identifier).build();
        return userServiceBlockingStub.getUserByIdentifier(request);  // Call the gRPC service
    }

    // Gracefully shutdown the channel
    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
