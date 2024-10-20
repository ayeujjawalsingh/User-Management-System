package com.ujjawal.user_management_system.notificationservice.repository;

import com.ujjawal.user_management_system.notificationservice.model.NotificationStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationStatusRepository extends JpaRepository<NotificationStatusModel, UUID> {

    List<NotificationStatusModel> findByUserId(UUID userId);
}
