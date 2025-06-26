package com.example.CloudStorage.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserLoginResponseDto {
    private UUID id;
    private String username;
    private String message;
}
