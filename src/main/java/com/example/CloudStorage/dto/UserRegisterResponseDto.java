package com.example.CloudStorage.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterResponseDto {
    public Long id;
    private String username;
    private String message;

}
