package com.example.CloudStorage.repository;

import com.example.CloudStorage.entity.UploadedFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<UploadedFileEntity, String> {
}
