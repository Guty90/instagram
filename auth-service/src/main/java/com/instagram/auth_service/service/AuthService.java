package com.instagram.auth_service.service;

import com.instagram.auth_service.dto.AuthRequestDTO;
import com.instagram.auth_service.dto.AuthResponseDTO;
import com.instagram.auth_service.dto.RegisterRequestDTO;
import com.instagram.auth_service.model.User;
import com.instagram.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }
        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username ya registrado");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User saved = repository.save(user);

        String token = jwtService.generateToken(saved.getId(), saved.getUsername(), saved.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .build();
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}