package com.instagram.notification_service.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class StoryDTO {
    private Long id;
    private Long userId;
    private String content;
}