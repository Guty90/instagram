package com.instagram.notification_service.repository;

import com.instagram.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToUserIdOrderByCreatedAtDesc(Long toUserId);
    Optional<Notification> findByStoryIdAndFromUserIdAndType(Long storyId, Long fromUserId, String type);
}