package com.example.CloudStorage.service;
import com.example.CloudStorage.dto.UploadedFileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.exception.FileNotFoundException;
import com.example.CloudStorage.mapper.SaveFileMapper;
import com.example.CloudStorage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final SaveFileMapper saveFileMapper;

    @Value("${file.upload.directory:uploads}")
    private String uploadDirectory;

    public UploadedFileDto uploadFile(MultipartFile multipartFile, UserEntity user) throws Exception {
        if(multipartFile.isEmpty()){
            throw new IllegalArgumentException("File can not be empty!");
        }
        Path uploadDir = Paths.get(uploadDirectory);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        String storedFileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        Path filepath = Paths.get(uploadDirectory, storedFileName);
        Files.copy(multipartFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

        UploadedFileEntity uploadedFile = new UploadedFileEntity();
        uploadedFile.setOriginalFileName(multipartFile.getOriginalFilename());
        uploadedFile.setStoredFileName(storedFileName);
        uploadedFile.setSize(multipartFile.getSize());
        uploadedFile.setUploadedAt(LocalDateTime.now());
        uploadedFile.setUser(user);

        UploadedFileEntity saved = fileRepository.save(uploadedFile);
        return saveFileMapper.toUploadedFileDto(saved);
    }

    public List<FileResponseDto> getAllFilesByUser(String userId){
        List<UploadedFileEntity> files = fileRepository.findByUserId(userId)
                .orElseThrow(() -> new FileNotFoundException("Any file couldnt find!"));
        return saveFileMapper.toDtoList(files);
    }

    public ResponseEntity<Resource> downloadFile(String id, String username) throws IOException {
        // 1. Does the file exist in the database?
        UploadedFileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File could not found."));
        // 2. Is the file belongs to that user?
        if (!file.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can not access this file!");
        }

        // 3. Is the file exist??
        // resolve() => combine paths like : uploadDirectory(/uploads) and storedFileName(/storedfilename) = /uploads/storedfilename
        Path path = Paths.get(uploadDirectory).resolve(file.getStoredFileName());
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File could not found on the server.");
        }

        Resource resource = new UrlResource(path.toUri());
        String contentType = Files.probeContentType(path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFileName() + "\"")
                .body(resource);
    }
}