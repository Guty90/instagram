package com.instagram.user_service.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private Long externalId;
    private String username;
    private String email;
}