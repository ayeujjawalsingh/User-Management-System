package com.ujjawal.user_management_system.notificationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_status")
@Data
public class NotificationStatusModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String mobileNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private NotificationSendStatus sendStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "mobile_notification_status")
    private NotificationStatus mobileNotificationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_notification_status")
    private NotificationStatus emailNotificationStatus;

    @Column(name = "mobile_notification_sent_at")
    private LocalDateTime mobileNotificationSentAt;

    @Column(name = "email_notification_sent_at")
    private LocalDateTime emailNotificationSentAt;

    @Column(name = "user_id")
    private UUID userId;

    // Enum definitions
    public enum NotificationSendStatus {
        PENDING,
        IN_PROGRESS,
        SENT,
        FAILED
    }

    public enum NotificationStatus {
        NOT_SENT,
        SENT,
        DELIVERED,
        READ,
        FAILED
    }
}
