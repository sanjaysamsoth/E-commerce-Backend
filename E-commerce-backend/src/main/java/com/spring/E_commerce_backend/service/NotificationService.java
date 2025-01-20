package com.spring.E_commerce_backend.service;

import com.spring.E_commerce_backend.models.Notification;
import com.spring.E_commerce_backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    public void broadcastNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void sendNotificationToUser(String userId, String message) {
        Notification notification = new Notification(userId, message, LocalDateTime.now(), false);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(userId, "/topic/notifications", message);
    }

    public List<Notification> getNotificationsByUser(String userId) {
        return notificationRepository.findByUserId(userId);
    }
}
