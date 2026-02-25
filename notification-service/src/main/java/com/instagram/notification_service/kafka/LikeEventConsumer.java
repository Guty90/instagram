package com.instagram.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagram.notification_service.dto.LikeEventDTO;
import com.instagram.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "story-liked", groupId = "notification-group")
    public void consume(String message) {
        try {
            LikeEventDTO event = objectMapper.readValue(message, LikeEventDTO.class);
            notificationService.handleLikeEvent(event);
        } catch (Exception e) {
            throw new RuntimeException("Error processing Kafka event: " + e.getMessage(), e);
        }
    }
}