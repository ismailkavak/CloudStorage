package com.example.CloudStorage.controller;

import com.example.CloudStorage.UserDetails.CustomUserDetails;
import com.example.CloudStorage.dto.FileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.service.CustomUserDetailsService;
import com.example.CloudStorage.service.FileService;
import com.example.CloudStorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/upload")
    public ResponseEntity<FileResponseDto> uploadFile(@RequestBody FileDto fileDto, @AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);
        FileResponseDto response = fileService.saveFile(fileDto, user);
        return ResponseEntity.ok(response);
    }
}
