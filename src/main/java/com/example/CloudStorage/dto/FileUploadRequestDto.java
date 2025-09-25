package com.example.CloudStorage.dto;

import lombok.Data;

@Data
public class FileUploadRequestDto {
    private String originalFileName;
    private String contentType;
    private Long size;
}
