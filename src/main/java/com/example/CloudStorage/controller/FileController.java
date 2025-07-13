package com.example.CloudStorage.controller;

import com.example.CloudStorage.dto.FileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<FileResponseDto> saveFile(@RequestBody FileDto fileDto){
        FileResponseDto response = fileService.saveFile(fileDto);
        return ResponseEntity.ok(response);
    }
}
