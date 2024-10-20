package com.ujjawal.user_management_system.notificationservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "notification_template")
@Data
public class NotificationTemplateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String slug;

    @Column(name = "sms_template", columnDefinition = "TEXT")
    private String smsTemplate;

    @Column(name = "sms_params")
    private String smsParams;

    @Column(name = "email_template", columnDefinition = "TEXT")
    private String emailTemplate;

    @Column(name = "email_params")
    private String emailParams;

    @Column(name = "email_subject")
    private String emailSubject;
}
