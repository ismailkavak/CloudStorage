package com.example.CloudStorage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFileEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String originalFileName;
    private String storedFileName;
    private Long size;
    private LocalDateTime uploadedAt;

    //@ManyToOne
    //@JoinColumn(name = "user_id")
    //private UserEntity user;
}
