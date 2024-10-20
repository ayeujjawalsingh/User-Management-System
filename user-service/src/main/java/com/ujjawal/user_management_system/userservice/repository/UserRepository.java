package com.ujjawal.user_management_system.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.ujjawal.user_management_system.userservice.model.UserModel;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    // Find active user by username
    Optional<UserModel> findByUsernameAndStatus(String username, String status);

    // Find active user by mobile number
    Optional<UserModel> findByMobileAndStatus(String mobile, String status);

    // Find active user by email
    Optional<UserModel> findByEmailAndStatus(String email, String status);

    // Find active user by userId
    Optional<UserModel> findByIdAndStatus(UUID Id, String status);

    // Check if an active user exists by username
    boolean existsByUsernameAndStatus(String username, String status);

    // Check if an active user exists by mobile number
    boolean existsByMobileAndStatus(String mobile, String status);

    // Check if an active user exists by email
    boolean existsByEmailAndStatus(String email, String status);
}
