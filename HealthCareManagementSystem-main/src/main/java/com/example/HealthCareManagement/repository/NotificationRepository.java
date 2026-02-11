package com.example.HealthCareManagement.repository;

import com.example.HealthCareManagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // This naming convention allows Spring to find notifications for any User
    List<Notification> findByRecipientId(Long userId);
}