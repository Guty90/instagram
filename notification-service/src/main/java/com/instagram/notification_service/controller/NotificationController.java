package com.instagram.notification_service.controller;

import com.instagram.notification_service.dto.NotificationResponseDTO;
import com.instagram.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping("/{toUserId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications(@PathVariable Long toUserId) {
        return ResponseEntity.ok(service.getNotifications(toUserId));
    }
}