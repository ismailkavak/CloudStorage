package com.example.CloudStorage.controller;

import com.example.CloudStorage.UserDetails.CustomUserDetails;
import com.example.CloudStorage.dto.*;
import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.service.FileService;
import com.example.CloudStorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @GetMapping("/file/{fileId}")
    public ResponseEntity<FileResponseDto> getFileById(@PathVariable String fileId){
        FileResponseDto file = fileService.getFileById(fileId);
        return ResponseEntity.ok(file);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileResponseDto>> getAllFilesByUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        List<FileResponseDto> response = fileService.getAllFilesByUser(user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
        String username = userDetails.getUsername();
        return fileService.downloadFile(id, username);
    }

    @GetMapping("/presignDownload/{id}")
    public ResponseEntity<URL> presignDownload(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
        String username = userDetails.getUsername();
        return fileService.downloadWithPresign(id, username);
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadedFileDto> uploadFile(@RequestParam("file")MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        UploadedFileDto response = fileService.uploadFile(file, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/presignUpload")
    public ResponseEntity<UploadedFileDto> presignUploadFile(@RequestBody FileUploadRequestDto fileUploadRequestDto,@AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        UploadedFileDto response = fileService.uploadFileWithPresign(fileUploadRequestDto, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/share/public/{fileId}")
    public ResponseEntity<String> createPublicShareLink(@PathVariable String fileId, @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        String userId = user.getId();
        return fileService.createPublicShareLink(fileId, userId);
    }

    @GetMapping("/files/share/{token}")
    public ResponseEntity<Resource> downloadFileByToken(@PathVariable String token) throws IOException{
        return fileService.downloadFileByToken(token);
    }

    @PostMapping("/file/share/{fileId}")
    public ResponseEntity<PrivateFileShareResponseDto> privateFileShare
            (@RequestBody PrivateFileShareRequestDto privateFileShareRequestDto, @PathVariable String fileId, @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException{
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        String sharedByUserId = user.getId();
        return fileService.privateFileShare(fileId, privateFileShareRequestDto, sharedByUserId);
    }

    @GetMapping("/get/files/shared-with-me")
    public ResponseEntity<List<PrivateFileShareResponseDto>> receivedFiles(@AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        return fileService.getFilesSharedWithMe(user.getId());
    }
    @GetMapping("get/files/sent")
    public ResponseEntity<List<PrivateFileShareResponseDto>> sentFiles(@AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        return fileService.getFilesSentByMe(user.getId());
    }

    @DeleteMapping("file/delete/{fileId}")
    public void deleteFile(@PathVariable String fileId){
        fileService.deleteFile(fileId);
    }
}