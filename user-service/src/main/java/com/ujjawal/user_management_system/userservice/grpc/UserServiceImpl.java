package com.ujjawal.user_management_system.userservice.grpc;

import com.ujjawal.user_management_system.userservice.grpc.UserRequest;
import com.ujjawal.user_management_system.userservice.grpc.UserResponse;
import com.ujjawal.user_management_system.userservice.grpc.UserServiceGrpc;
import com.ujjawal.user_management_system.userservice.repository.UserRepository;
import com.ujjawal.user_management_system.userservice.model.UserModel;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;


@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserByIdentifier(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        String identifier = request.getIdentifier();
        Optional<UserModel> userOptional;

        if (identifier.matches("^\\d{10,15}$")) {
            userOptional = userRepository.findByMobileAndStatus(identifier, "1");
        } else if (identifier.contains("@")) {
            userOptional = userRepository.findByEmailAndStatus(identifier, "1");
        } else {
            userOptional = userRepository.findByUsernameAndStatus(identifier, "1");
        }

        UserResponse.Builder responseBuilder = UserResponse.newBuilder();

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            responseBuilder.setUserExists(true)
                    .setUserId(user.getId().toString())
                    .setHashedPassword(user.getPassword());
        } else {
            responseBuilder.setUserExists(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}

