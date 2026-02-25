package com.instagram.notification_service.service;

import com.instagram.notification_service.dto.LikeEventDTO;
import com.instagram.notification_service.dto.NotificationResponseDTO;
import com.instagram.notification_service.dto.StoryDTO;
import com.instagram.notification_service.dto.UserDTO;
import com.instagram.notification_service.model.Notification;
import com.instagram.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final RestTemplate restTemplate;

    @Value("${story-service.url}")
    private String storyServiceUrl;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Transactional
    public void handleLikeEvent(LikeEventDTO event) {
        if ("LIKED".equals(event.getAction())) {

            // Valida que la historia existe
            StoryDTO story = restTemplate.getForObject(
                    storyServiceUrl + "/api/stories/" + event.getStoryId(),
                    StoryDTO.class
            );

            if (story == null) return;

            // Valida que el usuario existe
            UserDTO user = restTemplate.getForObject(
                    userServiceUrl + "/api/users/" + event.getUserId(),
                    UserDTO.class
            );

            if (user == null) return;

            Notification notification = Notification.builder()
                    .storyId(event.getStoryId())
                    .fromUserId(event.getUserId())
                    .toUserId(story.getUserId())
                    .type("LIKED")
                    .build();
            repository.save(notification);
        }

        if ("UNLIKED".equals(event.getAction())) {
            repository.deleteByStoryIdAndFromUserId(event.getStoryId(), event.getUserId());
        }
    }

    public List<NotificationResponseDTO> getNotifications(Long toUserId) {
        return repository.findByToUserIdOrderByCreatedAtDesc(toUserId)
                .stream()
                .map(n -> NotificationResponseDTO.builder()
                        .id(n.getId())
                        .storyId(n.getStoryId())
                        .fromUserId(n.getFromUserId())
                        .toUserId(n.getToUserId())
                        .type(n.getType())
                        .createdAt(n.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}