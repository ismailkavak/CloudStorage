package com.example.CloudStorage.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserRegisterResponseDto {
    private String id;
    private String username;
    private String message;
}