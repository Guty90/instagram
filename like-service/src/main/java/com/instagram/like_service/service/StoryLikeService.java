package com.instagram.like_service.service;

import com.instagram.like_service.dto.LikeAction;
import com.instagram.like_service.dto.LikeRequestDTO;
import com.instagram.like_service.dto.LikeResponseDTO;
import com.instagram.like_service.kafka.LikeEventProducer;
import com.instagram.like_service.model.StoryLike;
import com.instagram.like_service.repository.StoryLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryLikeService {

    private final StoryLikeRepository repository;
    private final LikeEventProducer producer;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;


    @Value("${story-service.url}")
    private String storyServiceUrl;

    @Value("${user-service.url}")
    private String userServiceUrl;

    private String redisKey(Long storyId) {
        return "likes:story:" + storyId;
    }

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

        // Valida que el usuario existe
        try {
            restTemplate.getForObject(
                    userServiceUrl + "/api/users/" + request.getUserId(),
                    Object.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Usuario no encontrado: " + request.getUserId());
        }

        String key = redisKey(request.getStoryId());
        String userId = request.getUserId().toString();

        if (repository.existsByStoryIdAndUserId(request.getStoryId(), request.getUserId())) {
            repository.deleteByStoryIdAndUserId(request.getStoryId(), request.getUserId());

            // Quita del set en Redis
            redisTemplate.opsForSet().remove(key, userId);

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

        // Agrega al set en Redis
        redisTemplate.opsForSet().add(key, userId);

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

    public boolean hasLiked(Long storyId, Long userId) {
        String key = redisKey(storyId);
        Boolean result = redisTemplate.opsForSet().isMember(key, userId.toString());

        // Si Redis no tiene el dato, consulta BD y sincroniza
        if (result == null) {
            boolean likedInDb = repository.existsByStoryIdAndUserId(storyId, userId);
            if (likedInDb) redisTemplate.opsForSet().add(key, userId.toString());
            return likedInDb;
        }
        return result;
    }

    public long getLikeCount(Long storyId) {
        Long count = redisTemplate.opsForSet().size(redisKey(storyId));
        if (count == null) return repository.countByStoryId(storyId);
        return count;
    }

    public List<Long> getUsersWhoLiked(Long storyId) {
        return repository.findByStoryId(storyId)
                .stream()
                .map(StoryLike::getUserId)
                .collect(Collectors.toList());
    }
}