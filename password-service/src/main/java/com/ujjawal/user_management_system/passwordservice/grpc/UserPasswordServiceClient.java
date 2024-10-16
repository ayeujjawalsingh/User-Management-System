package com.ujjawal.user_management_system.passwordservice.grpc;

import com.ujjawal.user_management_system.passwordservice.grpc.UserPasswordRequest;
import com.ujjawal.user_management_system.passwordservice.grpc.UserPasswordResponse;
import com.ujjawal.user_management_system.passwordservice.grpc.UserPasswordServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class UserPasswordServiceClient {

    private final ManagedChannel channel;
    private final UserPasswordServiceGrpc.UserPasswordServiceBlockingStub userPasswordServiceBlockingStub;

    public UserPasswordServiceClient() {
        // Initialize gRPC channel and stubs
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        this.userPasswordServiceBlockingStub = UserPasswordServiceGrpc.newBlockingStub(channel);
    }

    public UserPasswordResponse setPasswordByIdentifier(String identifier, String hashedPassword) {
        // Build the gRPC request for setting password
        UserPasswordRequest request = UserPasswordRequest.newBuilder()
                .setIdentifier(identifier)
                .setHashedPassword(hashedPassword)
                .build();
        return userPasswordServiceBlockingStub.setPasswordByIdentifier(request);  // Call the gRPC service
    }

    // Gracefully shutdown the channel
    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
