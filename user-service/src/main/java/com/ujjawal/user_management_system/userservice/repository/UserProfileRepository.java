package com.ujjawal.user_management_system.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

import com.ujjawal.user_management_system.userservice.model.UserProfileModel;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileModel, UUID> {

    // Fetch user details by userId where status is active
    @Query("SELECT ud FROM UserProfileModel ud WHERE ud.user = :userId AND ud.status = '1'")
    Optional<UserProfileModel> findActiveByUserId(@Param("userId") UUID userId);

    // Check if active user details exist by userId
    @Query("SELECT COUNT(ud) > 0 FROM UserProfileModel ud WHERE ud.user = :userId AND ud.status = '1'")
    boolean existsActiveByUserId(@Param("userId") UUID userId);

}