package com.example.CloudStorage.repository;

import com.example.CloudStorage.entity.PrivateFileShareEntity;
import com.example.CloudStorage.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrivateFileShareRepository extends JpaRepository<PrivateFileShareEntity, String> {
    Optional<List<PrivateFileShareEntity>> findBySharedWithId(String id);
    Optional<List<PrivateFileShareEntity>> findBySharedById(String id);
    Optional<PrivateFileShareEntity> findBySharedFileIdAndSharedWithId(String fileId, String sharedWithUserId);
}
