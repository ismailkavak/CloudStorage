package com.example.CloudStorage.dto;

import lombok.Data;

@Data
public class FileResponseDto {
    private String id;
    private String originalFileName;
    private String storedFileName;
}
