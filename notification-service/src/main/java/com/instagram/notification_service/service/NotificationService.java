package com.instagram.notification_service.service;

import com.instagram.notification_service.dto.LikeEventDTO;
import com.instagram.notification_service.dto.NotificationResponseDTO;
import com.instagram.notification_service.model.Notification;
import com.instagram.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    @Transactional
    public void handleLikeEvent(LikeEventDTO event) {
        if ("LIKED".equals(event.getAction())) {
            // Por ahora toUserId = storyId hasta tener user-service
            // En el futuro aquí consultarías user-service para saber el dueño
            Notification notification = Notification.builder()
                    .storyId(event.getStoryId())
                    .fromUserId(event.getUserId())
                    .toUserId(event.getStoryId()) // temporal
                    .type("LIKED")
                    .build();
            repository.save(notification);
        }

        if ("UNLIKED".equals(event.getAction())) {
            repository.findByStoryIdAndFromUserIdAndType(
                    event.getStoryId(), event.getUserId(), "LIKED"
            ).ifPresent(repository::delete);
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