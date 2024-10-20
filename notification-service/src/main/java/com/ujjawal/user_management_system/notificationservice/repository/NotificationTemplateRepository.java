package com.ujjawal.user_management_system.notificationservice.repository;

import com.ujjawal.user_management_system.notificationservice.model.NotificationTemplateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplateModel, UUID> {

    Optional<NotificationTemplateModel> findBySlug(String slug);
}
