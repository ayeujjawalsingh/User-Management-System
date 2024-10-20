package com.ujjawal.user_management_system.otpservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import com.ujjawal.user_management_system.otpservice.grpc.NotificationRequest;
import com.ujjawal.user_management_system.otpservice.grpc.NotificationResponse;
import com.ujjawal.user_management_system.otpservice.grpc.NotificationServiceGrpc;

import java.util.Map;

@Service // Add this annotation to make it a Spring-managed bean
public class NotificationServiceClient {
    private final NotificationServiceGrpc.NotificationServiceBlockingStub notificationServiceBlockingStub;

    public NotificationServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9099)
                .usePlaintext()
                .build();
        notificationServiceBlockingStub = NotificationServiceGrpc.newBlockingStub(channel);
    }

    public NotificationResponse sendNotificationByIdentifier(String mobile, String email, String slug, Map<String, String> additional_data) {
        System.out.println("Entered sendNotificationByIdentifier method");
        NotificationRequest request = NotificationRequest.newBuilder()
                .setMobile(mobile)
                .setEmail(email)
                .setSlug(slug)
                .putAllAdditionalData(additional_data)
                .build();
        System.out.println("request: " + request);
        return notificationServiceBlockingStub.sendNotificationByIdentifier(request);
    }
}
