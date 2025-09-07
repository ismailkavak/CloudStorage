package com.example.CloudStorage.dto;

import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.entity.UserEntity;
import lombok.Data;

@Data
public class PrivateFileShareResponseDto {
    private String id;
    private UserEntity sharedBy;
    private UserEntity sharedWith;
    private UploadedFileEntity sharedFile;
}
