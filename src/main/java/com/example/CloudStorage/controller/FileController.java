package com.example.CloudStorage.controller;

import com.example.CloudStorage.UserDetails.CustomUserDetails;
import com.example.CloudStorage.dto.FileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.service.CustomUserDetailsService;
import com.example.CloudStorage.service.FileService;
import com.example.CloudStorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponseDto> uploadFile(@RequestBody FileDto fileDto, @AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        UserEntity user = userService.getUserByUsername(username);

        FileResponseDto response = fileService.saveFile(fileDto, user);
        return ResponseEntity.ok(response);
    }
}
