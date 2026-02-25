package com.instagram.like_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponseDTO {
    private Long id;
    private Long storyId;
    private Long userId;
    private LocalDateTime createdAt;
    private LikeAction action;
}