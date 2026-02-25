package com.instagram.story_service.controller;

import com.instagram.story_service.dto.StoryRequestDTO;
import com.instagram.story_service.dto.StoryResponseDTO;
import com.instagram.story_service.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService service;

    @PostMapping
    public ResponseEntity<StoryResponseDTO> create(@RequestBody StoryRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createStory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStoryById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<StoryResponseDTO>> getActive() {
        return ResponseEntity.ok(service.getActiveStories());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StoryResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getStoriesByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStory(id);
        return ResponseEntity.noContent().build();
    }
}