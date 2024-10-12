//package com.ujjawal.user_management_system.authservice;
//
//import com.ujjawal.user_management_system.userservice.grpc.UserServiceGrpc;
//import com.ujjawal.user_management_system.userservice.grpc.UserRequest;
//import com.ujjawal.user_management_system.userservice.grpc.UserResponse;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class AuthServiceGrpcTest {
//
//    private ManagedChannel channel;
//    private UserServiceGrpc.UserServiceBlockingStub blockingStub;
//
//    @BeforeEach
//    public void setup() {
//        // Connect to the gRPC server (user-service)
//        channel = ManagedChannelBuilder.forAddress("localhost", 8081) // The user-service port
//                .usePlaintext()
//                .build();
//        blockingStub = UserServiceGrpc.newBlockingStub(channel);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        channel.shutdown();
//    }
//
//    @Test
//    public void testCheckUserExists() {
//        // Prepare a request to the user-service
//        UserRequest request = UserRequest.newBuilder()
//                .setUsername("ujjawal")
//                .build();
//
//        // Call the service
//        UserResponse response = blockingStub.checkUserExists(request);
//
//        // Validate the response
//        assertEquals(true, response.getExists());
//    }
//}
