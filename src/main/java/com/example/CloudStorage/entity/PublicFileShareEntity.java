package com.example.CloudStorage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicFileShareEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(unique = true)
    private String shareToken;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private UploadedFileEntity uploadedFile;

    @ManyToOne
    @JoinColumn(name = "shared_by_user_id")
    private UserEntity sharedBy;
}