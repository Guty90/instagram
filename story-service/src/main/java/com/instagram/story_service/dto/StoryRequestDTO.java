package com.instagram.story_service.dto;

import lombok.Data;

@Data
public class StoryRequestDTO {
    private Long userId;
    private String content;
}