package com.instagram.notification_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LikeEventDTO {
    private Long id;
    private Long storyId;
    private Long userId;
    private LocalDateTime createdAt;
    private String action; // LIKED o UNLIKED
}