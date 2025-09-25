package com.example.CloudStorage.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadedFileDto {
    private String id;
    private String originalFileName;
    private String storedFileName;
    private Long size;
    private LocalDateTime uploadedAt;
    private String url;
}
