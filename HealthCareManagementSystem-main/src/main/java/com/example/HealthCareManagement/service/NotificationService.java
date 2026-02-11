package com.example.HealthCareManagement.service;

import com.example.HealthCareManagement.entity.Notification;
import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepo;

    public NotificationService(NotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public void createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setRecipient(user);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepo.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepo.findByRecipientId(userId);
    }
}