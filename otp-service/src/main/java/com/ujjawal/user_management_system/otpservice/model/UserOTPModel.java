package com.ujjawal.user_management_system.otpservice.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_otp")
public class UserOTPModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String otp;

    @Column(name = "expiration_time", nullable = false)
    private Instant expirationTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OTPStatus status = OTPStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Default constructor
    public UserOTPModel() {
    }

    // Parameterized constructor
    public UserOTPModel(UUID userId, String otp, Instant expirationTime) {
        this.userId = userId;
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.status = OTPStatus.ACTIVE;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OTPStatus getStatus() {
        return status;
    }

    public void setStatus(OTPStatus status) {
        this.status = status;
    }

    // Enum for otp status
    public enum OTPStatus {
        ACTIVE, REVOKED, EXPIRED
    }
}
