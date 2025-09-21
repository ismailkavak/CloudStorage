package com.example.CloudStorage.service;
import com.example.CloudStorage.dto.*;
import com.example.CloudStorage.entity.PrivateFileShareEntity;
import com.example.CloudStorage.entity.PublicFileShareEntity;
import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.exception.FileNotFoundException;
import com.example.CloudStorage.mapper.PrivateFileShareMapper;
import com.example.CloudStorage.mapper.PublicFileShareMapper;
import com.example.CloudStorage.mapper.SaveFileMapper;
import com.example.CloudStorage.repository.FileRepository;
import com.example.CloudStorage.repository.PrivateFileShareRepository;
import com.example.CloudStorage.repository.PublicFileShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final SaveFileMapper saveFileMapper;
    private final PublicFileShareRepository publicFileShareRepository;
    private final UserService userService;
    private final PublicFileShareMapper publicFileShareMapper;
    private final PrivateFileShareRepository privateFileShareRepository;
    private final PrivateFileShareMapper privateFileShareMapper;

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

    public FileResponseDto getFileById(String fileId){
        UploadedFileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("Any file could not found!"));
        return saveFileMapper.toResponseDto(file);
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
            System.out.println(path);
            System.out.println(uploadDirectory);
            System.out.println(file.getStoredFileName());
            throw new FileNotFoundException("File could not found on the server.");
        }

        Resource resource = new UrlResource(path.toUri());
        String contentType = Files.probeContentType(path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFileName() + "\"")
                .body(resource);
    }

    public ResponseEntity<String> createPublicShareLink(String fileId, String userId) throws  IOException{
        UploadedFileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File could not found."));

        if (!file.getUser().getId().equals(userId)){
            throw new AccessDeniedException("You can not access this file!");
        }

        String token = UUID.randomUUID().toString();

        UserEntity user = userService.getUserById(userId);

        PublicFileShareEntity publicFileShareEntity = new PublicFileShareEntity();
        publicFileShareEntity.setShareToken(token);
        publicFileShareEntity.setCreatedAt(LocalDateTime.now());
        publicFileShareEntity.setUploadedFile(file);
        publicFileShareEntity.setSharedBy(user);
        publicFileShareEntity.setExpiredAt(LocalDateTime.now().plusDays(1));

        PublicFileShareEntity savedEntity = publicFileShareRepository.save(publicFileShareEntity);
        PublicFileShareDto responseDto =  publicFileShareMapper.toPublicFileShareDto(savedEntity);

        String baseUrl = "http://localhost:8080";
        responseDto.setShareUrl(baseUrl + "/files/share/" + savedEntity.getShareToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto.getShareUrl());
    }

    public ResponseEntity<Resource> downloadFileByToken(String token) throws IOException{
       PublicFileShareEntity shareEntity = publicFileShareRepository.findByShareToken(token)
                .orElseThrow(() -> new FileNotFoundException("File could not found."));

       UploadedFileEntity file = shareEntity.getUploadedFile();

       boolean isTokenExpired = shareEntity.getExpiredAt().isBefore(LocalDateTime.now());

       if (isTokenExpired) {
           throw new IllegalStateException("This link has expired!");
       }

       Path path = Paths.get("uploads").resolve(file.getStoredFileName());

       if(!Files.exists(path)){
           System.out.println(path);
           System.out.println(token);
            throw new FileNotFoundException("File could not be found in the server.");
       }

       Resource resource = new UrlResource(path.toUri());
       String contentType = Files.probeContentType(path);

       return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFileName() + "\"")
                .body(resource);
    }

    public ResponseEntity<PrivateFileShareResponseDto> privateFileShare
            (String fileId, PrivateFileShareRequestDto privateFileShareRequestDto, String sharedByUserId ) throws IOException{
        UploadedFileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File could not found!"));

        if (!file.getUser().getId().equals(sharedByUserId)){
            throw new AccessDeniedException("You can not access this file!");
        }

        UserEntity sharedByUser = userService.getUserById(sharedByUserId);
        UserEntity sharedWithUser = userService.getUserByUsername(privateFileShareRequestDto.getSharedWithUsername());

        Optional<PrivateFileShareEntity> existingShare = privateFileShareRepository.findBySharedFileIdAndSharedWithId
                (file.getId(), sharedWithUser.getId());

        if(existingShare.isPresent()){
            PrivateFileShareResponseDto responseDto = privateFileShareMapper.toResponseDto(existingShare.get());
            return ResponseEntity.ok(responseDto);
        }

        PrivateFileShareEntity shareEntity = new PrivateFileShareEntity();
        shareEntity.setSharedFile(file);
        shareEntity.setSharedBy(sharedByUser);
        shareEntity.setSharedWith(sharedWithUser);
        shareEntity.setCreatedAt(LocalDateTime.now());

        PrivateFileShareEntity savedEntity = privateFileShareRepository.save(shareEntity);
        PrivateFileShareResponseDto toResponseDto = privateFileShareMapper.toResponseDto(savedEntity);

        return ResponseEntity.ok(toResponseDto);
    }

    public ResponseEntity<List<PrivateFileShareResponseDto>> getFilesSharedWithMe(String id) {
        List<PrivateFileShareEntity> sharedFiles = privateFileShareRepository.findBySharedWithId(id)
                .orElseThrow(() -> new FileNotFoundException("File could not found!"));

        return ResponseEntity.ok(privateFileShareMapper.toResponseDtoList(sharedFiles));
    }

    public ResponseEntity<List<PrivateFileShareResponseDto>> getFilesSentByMe(String id) {
        List<PrivateFileShareEntity> sentFiles = privateFileShareRepository.findBySharedById(id)
                .orElseThrow(() -> new FileNotFoundException("File could not found!"));
        return ResponseEntity.ok(privateFileShareMapper.toResponseDtoList(sentFiles));
    }


    public void deleteFile(String fileId) {
        UploadedFileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Could not delete!"));

        fileRepository.delete(file);
    }
}