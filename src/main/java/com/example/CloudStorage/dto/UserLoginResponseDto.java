package com.example.CloudStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserLoginResponseDto {
    private String token;
}
