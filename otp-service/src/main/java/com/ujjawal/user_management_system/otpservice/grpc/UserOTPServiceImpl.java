package com.ujjawal.user_management_system.otpservice.grpc;

import com.ujjawal.user_management_system.otpservice.grpc.UserOTPRequest;
import com.ujjawal.user_management_system.otpservice.grpc.UserOTPResponse;
import com.ujjawal.user_management_system.otpservice.grpc.UserOTPServiceGrpc;
import com.ujjawal.user_management_system.otpservice.repository.UserOTPRepository;
import com.ujjawal.user_management_system.otpservice.model.UserOTPModel;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class UserOTPServiceImpl extends UserOTPServiceGrpc.UserOTPServiceImplBase{
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    @Autowired
    private UserOTPRepository userOTPRepository;

    public boolean verifyOtp(String input, String hashed) {
        if (input == null || hashed == null) {
            return false; // Added a null check for safety
        }
        return ARGON2.verify(hashed, input.trim().toCharArray());
    }

    @Override
    public void verifyOTPByUserID(UserOTPRequest request, StreamObserver<UserOTPResponse> responseObserver){
        UUID userID = UUID.fromString(request.getUserId());
        String otp = request.getOtp();
        Optional<UserOTPModel> userOptional;
        userOptional = userOTPRepository.findByUserIdAndStatus(userID, UserOTPModel.OTPStatus.ACTIVE);

        UserOTPResponse.Builder responseBuilder = UserOTPResponse.newBuilder();

        if (userOptional.isPresent()) {
            UserOTPModel user = userOptional.get();
            String hashedOtp = user.getOtp();

            if (!verifyOtp(otp, hashedOtp)) {
                responseBuilder.setOtpVerified(false);
            }
            else{
                if (Instant.now().isBefore(user.getExpirationTime())) {
                    user.setOtp("");
                    user.setStatus(UserOTPModel.OTPStatus.EXPIRED);
                    userOTPRepository.save(user);
                    responseBuilder.setOtpVerified(true);
                }else{
                    responseBuilder.setOtpVerified(false);
                }
            }
        } else {
            responseBuilder.setOtpVerified(false);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }


}
