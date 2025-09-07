package com.example.CloudStorage.dto;

import com.example.CloudStorage.entity.UserEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PublicFileShareDto {
    private String shareUrl;
    private LocalDateTime createdAt;
    private UserEntity sharedBy;
    private Date expiredAt;
}
