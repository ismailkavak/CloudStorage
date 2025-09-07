package com.example.CloudStorage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateFileShareEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne()
    @JoinColumn(name = "shared_by_user_id")
    private UserEntity sharedBy;

    @ManyToOne()
    @JoinColumn(name = "shared_with_user_id")
    private UserEntity sharedWith;

    @ManyToOne()
    @JoinColumn(name = "file_id", unique = true)
    private UploadedFileEntity sharedFile;

    private LocalDateTime createdAt;
}