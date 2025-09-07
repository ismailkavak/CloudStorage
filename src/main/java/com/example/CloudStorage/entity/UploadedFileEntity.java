package com.example.CloudStorage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "uploadedFile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PublicFileShareEntity> publicFileShareEntity = new ArrayList<>();

    @OneToMany(mappedBy = "sharedFile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PrivateFileShareEntity> privateFileShareEntity = new ArrayList<>();
}