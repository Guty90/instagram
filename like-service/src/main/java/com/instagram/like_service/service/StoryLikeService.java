package com.instagram.like_service.service;

import com.instagram.like_service.dto.LikeAction;
import com.instagram.like_service.dto.LikeRequestDTO;
import com.instagram.like_service.dto.LikeResponseDTO;
import com.instagram.like_service.kafka.LikeEventProducer;
import com.instagram.like_service.model.StoryLike;
import com.instagram.like_service.repository.StoryLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class StoryLikeService {

    private final StoryLikeRepository repository;
    private final LikeEventProducer producer;
    private final RestTemplate restTemplate;

    @Value("${story-service.url}")
    private String storyServiceUrl;

    @Transactional
    public LikeResponseDTO toggleLike(LikeRequestDTO request) {

        // Valida que la historia existe
        try {
            restTemplate.getForObject(
                    storyServiceUrl + "/api/stories/" + request.getStoryId(),
                    Object.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Historia no encontrada: " + request.getStoryId());
        }

        if (repository.existsByStoryIdAndUserId(request.getStoryId(), request.getUserId())) {
            repository.deleteByStoryIdAndUserId(request.getStoryId(), request.getUserId());

            LikeResponseDTO unlikeEvent = LikeResponseDTO.builder()
                    .storyId(request.getStoryId())
                    .userId(request.getUserId())
                    .action(LikeAction.UNLIKED)
                    .build();
            producer.sendLikeEvent(unlikeEvent);
            return null;
        }

        StoryLike saved = repository.save(StoryLike.builder()
                .storyId(request.getStoryId())
                .userId(request.getUserId())
                .build());

        LikeResponseDTO response = LikeResponseDTO.builder()
                .id(saved.getId())
                .storyId(saved.getStoryId())
                .userId(saved.getUserId())
                .createdAt(saved.getCreatedAt())
                .action(LikeAction.LIKED)
                .build();

        producer.sendLikeEvent(response);
        return response;
    }

    public long getLikeCount(Long storyId) {
        return repository.countByStoryId(storyId);
    }
}