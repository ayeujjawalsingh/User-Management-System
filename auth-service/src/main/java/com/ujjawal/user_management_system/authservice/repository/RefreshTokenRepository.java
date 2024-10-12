package com.ujjawal.user_management_system.authservice.repository;

import com.ujjawal.user_management_system.authservice.model.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {
    Optional<RefreshTokenModel> findByToken(String token);
    int deleteByUserId(UUID userId);
}