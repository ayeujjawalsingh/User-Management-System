package com.ujjawal.user_management_system.userservice.grpc;

import com.ujjawal.user_management_system.userservice.grpc.UserPasswordRequest;
import com.ujjawal.user_management_system.userservice.grpc.UserPasswordResponse;
import com.ujjawal.user_management_system.userservice.grpc.UserPasswordServiceGrpc;
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
public class UserPasswordServiceImpl extends UserPasswordServiceGrpc.UserPasswordServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void setPasswordByIdentifier(UserPasswordRequest request, StreamObserver<UserPasswordResponse> responseObserver) {
        String identifier = request.getIdentifier();
        String hashedPassword = request.getHashedPassword();
        Optional<UserModel> userOptional;

        if (identifier.matches("^\\d{10,15}$")) {
            userOptional = userRepository.findByMobileAndStatus(identifier, "1");
        } else if (identifier.contains("@")) {
            userOptional = userRepository.findByEmailAndStatus(identifier, "1");
        } else {
            userOptional = userRepository.findByUsernameAndStatus(identifier, "1");
        }

        UserPasswordResponse.Builder responseBuilder = UserPasswordResponse.newBuilder();

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            user.setPassword(hashedPassword);
            userRepository.save(user);

            responseBuilder.setUserExists(true)
                    .setPasswordSaved(true);
        } else {
            responseBuilder.setUserExists(false)
                    .setPasswordSaved(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}

