package com.spring.E_commerce_backend.controller;

import com.spring.E_commerce_backend.models.Notification;
import com.spring.E_commerce_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/notifications/broadcast")
    public void broadcastNotification(@RequestBody String message) {
        notificationService.broadcastNotification(message);
    }

    @PostMapping("/notifications/send")
    public void sendNotificationToUser(@RequestParam String userId, @RequestBody String message) {
        if (userId == null || userId.isEmpty() || message == null || message.isEmpty()) {
            throw new IllegalArgumentException("userId and message must not be null or empty");
        }
        notificationService.sendNotificationToUser(userId, message);
    }

    @GetMapping("/notifications/{userId}")
    public List<Notification> getUserNotifications(@PathVariable String userId) {
        return notificationService.getNotificationsByUser(userId);
    }

    @MessageMapping("/send-notification")
    @SendTo("/topic/notifications")
    public String handleWebSocketNotification(String message) {
        return message;
    }
}
