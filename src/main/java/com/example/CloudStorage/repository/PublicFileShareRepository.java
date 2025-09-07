package com.example.CloudStorage.repository;

import com.example.CloudStorage.entity.PublicFileShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicFileShareRepository extends JpaRepository<PublicFileShareEntity, String> {
    Optional<PublicFileShareEntity> findByShareToken(String token);
}
