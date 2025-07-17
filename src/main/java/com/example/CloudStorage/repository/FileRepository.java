package com.example.CloudStorage.repository;

import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<UploadedFileEntity, String> {
    Optional<List<UploadedFileEntity>> findByUserId(String id);

    Optional<UploadedFileEntity> findByStoredFileName(String storedFileName);
}
