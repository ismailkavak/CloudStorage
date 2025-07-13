package com.example.CloudStorage.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserLoginResponseDto {
    private String token;
}
