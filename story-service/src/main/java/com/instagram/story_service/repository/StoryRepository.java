package com.instagram.story_service.repository;

import com.instagram.story_service.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByUserId(Long userId);
    List<Story> findByExpiresAtAfter(LocalDateTime now); // solo historias activas
}