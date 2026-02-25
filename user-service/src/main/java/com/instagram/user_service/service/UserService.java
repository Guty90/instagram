package com.instagram.user_service.service;

import com.instagram.user_service.dto.UserRequestDTO;
import com.instagram.user_service.dto.UserResponseDTO;
import com.instagram.user_service.model.User;
import com.instagram.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = User.builder()
                .externalId(request.getExternalId())
                .username(request.getUsername())
                .email(request.getEmail())
                .build();

        return toDTO(repository.save(user));
    }

    public UserResponseDTO getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        return toDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .externalId(user.getExternalId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}