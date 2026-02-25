package com.instagram.user_service.controller;

import com.instagram.user_service.dto.UserRequestDTO;
import com.instagram.user_service.dto.UserResponseDTO;
import com.instagram.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}