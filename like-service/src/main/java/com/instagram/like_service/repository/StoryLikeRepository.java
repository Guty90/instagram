package com.instagram.like_service.repository;

import com.instagram.like_service.model.StoryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
    boolean existsByStoryIdAndUserId(Long storyId, Long userId);
    void deleteByStoryIdAndUserId(Long storyId, Long userId);
    long countByStoryId(Long storyId);
}