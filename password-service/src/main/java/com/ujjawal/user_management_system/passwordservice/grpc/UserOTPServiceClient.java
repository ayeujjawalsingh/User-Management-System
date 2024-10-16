package com.ujjawal.user_management_system.passwordservice.grpc;

import com.ujjawal.user_management_system.passwordservice.grpc.UserOTPRequest;
import com.ujjawal.user_management_system.passwordservice.grpc.UserOTPResponse;
import com.ujjawal.user_management_system.passwordservice.grpc.UserOTPServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class UserOTPServiceClient {

    private final ManagedChannel channel;
    private final UserOTPServiceGrpc.UserOTPServiceBlockingStub userOTPServiceBlockingStub;

    public UserOTPServiceClient() {
        // Initialize gRPC channel and stubs
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9095)
                .usePlaintext()
                .build();
        this.userOTPServiceBlockingStub = UserOTPServiceGrpc.newBlockingStub(channel);
    }

    public UserOTPResponse verifyOTPByUserID(String userID, String otp) {
        // Build the gRPC request for setting OTP
        System.out.println("getOTPByUserID Reached.....");
        System.out.println("userID: " + userID);
        System.out.println("userOTPServiceBlockingStub: " + userOTPServiceBlockingStub);
        UserOTPRequest request = UserOTPRequest.newBuilder()
                .setUserId(userID)
                .setOtp(otp)
                .build();
        System.out.println("request: " + request);
        return userOTPServiceBlockingStub.verifyOTPByUserID(request);  // Call the gRPC service
    }

    // Gracefully shutdown the channel
    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}
