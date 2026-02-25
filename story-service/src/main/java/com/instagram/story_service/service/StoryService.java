package com.instagram.story_service.service;

import com.instagram.story_service.dto.StoryRequestDTO;
import com.instagram.story_service.dto.StoryResponseDTO;
import com.instagram.story_service.dto.UserDTO;
import com.instagram.story_service.model.Story;
import com.instagram.story_service.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository repository;
    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;

    public StoryResponseDTO createStory(StoryRequestDTO request) {
        // Valida que el usuario existe
        Object user = restTemplate.getForObject(
                userServiceUrl + "/api/users/" + request.getUserId(),
                Object.class
        );
        if (user == null) throw new RuntimeException("Usuario no encontrado: " + request.getUserId());

        Story story = Story.builder()
                .userId(request.getUserId())
                .content(request.getContent())
                .build();

        return toDTO(repository.save(story));
    }

    public void deleteStory(Long id) {
        Story story = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada: " + id));
        repository.delete(story);
    }

    public StoryResponseDTO getStoryById(Long id) {
        Story story = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada: " + id));
        return toDTO(story);
    }

    public List<StoryResponseDTO> getActiveStories() {
        return repository.findByExpiresAtAfter(LocalDateTime.now())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<StoryResponseDTO> getStoriesByUser(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private StoryResponseDTO toDTO(Story story) {
        String username = null;
        try {
            UserDTO user = restTemplate.getForObject(
                    userServiceUrl + "/api/users/" + story.getUserId(), UserDTO.class);
            if (user != null) username = user.getUsername();
        } catch (Exception ignored) {}

        return StoryResponseDTO.builder()
                .id(story.getId())
                .userId(story.getUserId())
                .username(username)
                .content(story.getContent())
                .createdAt(story.getCreatedAt())
                .expiresAt(story.getExpiresAt())
                .expired(story.getExpiresAt().isBefore(LocalDateTime.now()))
                .build();
    }
}