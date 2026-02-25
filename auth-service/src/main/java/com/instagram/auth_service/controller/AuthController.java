package com.instagram.auth_service.controller;

import com.instagram.auth_service.dto.AuthRequestDTO;
import com.instagram.auth_service.dto.AuthResponseDTO;
import com.instagram.auth_service.dto.RegisterRequestDTO;
import com.instagram.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(service.login(request));
    }
}
