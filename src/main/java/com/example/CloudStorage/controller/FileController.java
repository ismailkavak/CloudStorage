package com.example.CloudStorage.controller;

import com.example.CloudStorage.UserDetails.CustomUserDetails;
import com.example.CloudStorage.dto.UploadedFileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.service.FileService;
import com.example.CloudStorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @GetMapping("/files")
    public ResponseEntity<List<FileResponseDto>> getAllFilesByUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        List<FileResponseDto> response = fileService.getAllFilesByUser(user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{storedFileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String storedFileName, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
        String username = userDetails.getUsername();
        return fileService.downloadFile(storedFileName, username);
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadedFileDto> uploadFile(@RequestParam("file")MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        String username = userDetails.getUsername();
        UserEntity user =userService.getUserByUsername(username);
        UploadedFileDto response = fileService.uploadFile(file, user);
        return ResponseEntity.ok(response);
    }
}
