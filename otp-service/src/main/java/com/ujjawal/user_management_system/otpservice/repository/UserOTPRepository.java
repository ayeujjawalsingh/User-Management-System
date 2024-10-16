package com.ujjawal.user_management_system.otpservice.repository;

import com.ujjawal.user_management_system.otpservice.model.UserOTPModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTPModel, UUID> {

    Optional<UserOTPModel> findByUserIdAndStatus(UUID userId, UserOTPModel.OTPStatus status);

    @Query("SELECT o FROM UserOTPModel o WHERE o.userId = :userId AND o.status = 'ACTIVE'")
    List<UserOTPModel> findActiveOTPsByUserId(@Param("userId") UUID userId);

}
