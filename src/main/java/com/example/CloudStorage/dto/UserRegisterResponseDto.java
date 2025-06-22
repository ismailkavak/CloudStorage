package com.example.CloudStorage.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserRegisterResponseDto {
    private UUID id;
    private String username;
    private String message;

}
