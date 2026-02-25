package com.instagram.like_service.controller;

import com.instagram.like_service.dto.LikeRequestDTO;
import com.instagram.like_service.dto.LikeResponseDTO;
import com.instagram.like_service.service.StoryLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes/stories")
@RequiredArgsConstructor
public class StoryLikeController {

    private final StoryLikeService service;

    // Un solo endpoint para dar y quitar like
    @PostMapping("/toggle")
    public ResponseEntity<LikeResponseDTO> toggle(@RequestBody LikeRequestDTO request) {
        LikeResponseDTO response = service.toggleLike(request);

        if (response == null) {
            return ResponseEntity.noContent().build(); // 204 - se quitó el like
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 - se dio like
    }

    @GetMapping("/{storyId}/count")
    public ResponseEntity<Long> count(@PathVariable Long storyId) {
        return ResponseEntity.ok(service.getLikeCount(storyId));
    }
}