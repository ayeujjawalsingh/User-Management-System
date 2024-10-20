package com.ujjawal.user_management_system.userservice.grpc;

import com.ujjawal.user_management_system.userservice.grpc.UserIdRequest;
import com.ujjawal.user_management_system.userservice.grpc.UserIdResponse;
import com.ujjawal.user_management_system.userservice.grpc.UserIdServiceGrpc;
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
public class UserIdServiceImpl extends UserIdServiceGrpc.UserIdServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validateUserByUserId(UserIdRequest request, StreamObserver<UserIdResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        Optional<UserModel> userOptional;


        userOptional = userRepository.findByIdAndStatus(userId, "1");

        UserIdResponse.Builder responseBuilder = UserIdResponse.newBuilder();

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            responseBuilder.setUserExists(true);
        } else {
            responseBuilder.setUserExists(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
