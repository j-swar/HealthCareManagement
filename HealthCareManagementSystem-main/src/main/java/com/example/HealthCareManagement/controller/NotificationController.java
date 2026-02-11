package com.example.HealthCareManagement.controller;

import com.example.HealthCareManagement.entity.Notification;
import com.example.HealthCareManagement.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepo;

    public NotificationController(NotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @GetMapping("/{userId}")
    public List<Notification> getNotifications(@PathVariable Long userId) {
        // Calling the synchronized method name from the repository
        return notificationRepo.findByRecipientId(userId);
    }
}