package com.ujjawal.user_management_system.userservice.grpc;

import com.ujjawal.user_management_system.userservice.grpc.UserRequest;
import com.ujjawal.user_management_system.userservice.grpc.UserResponse;
import com.ujjawal.user_management_system.userservice.grpc.UserServiceGrpc;
import com.ujjawal.user_management_system.userservice.repository.UserRepository;
import com.ujjawal.user_management_system.userservice.model.UserModel;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserByIdentifier(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println("Entered into user-service with identifier: " + request.getIdentifier());
        String identifier = request.getIdentifier();
        Optional<UserModel> userOptional;

        if (identifier.matches("^\\d{10,15}$")) {
            // It's a mobile number
            userOptional = userRepository.findByMobileAndStatus(identifier, "1");
        } else if (identifier.contains("@")) {
            // It's an email
            userOptional = userRepository.findByEmailAndStatus(identifier, "1");
        } else {
            // It's a username
            userOptional = userRepository.findByUsernameAndStatus(identifier, "1");
        }

        UserResponse.Builder responseBuilder = UserResponse.newBuilder();

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            responseBuilder.setUserExists(true)
                    .setHashedPassword(user.getPassword());
        } else {
            responseBuilder.setUserExists(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
