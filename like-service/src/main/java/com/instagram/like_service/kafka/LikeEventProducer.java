package com.instagram.like_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagram.like_service.dto.LikeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "story-liked";

    public void sendLikeEvent(LikeResponseDTO likeResponse) {
        try {
            String json = objectMapper.writeValueAsString(likeResponse);
            kafkaTemplate.send(TOPIC, json);
        } catch (Exception e) {
            throw new RuntimeException("Error sending Kafka event: " + e.getMessage(), e);
        }
    }
}