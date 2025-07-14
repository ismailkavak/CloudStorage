package com.example.CloudStorage.service;

import com.example.CloudStorage.dto.FileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.mapper.SaveFileMapper;
import com.example.CloudStorage.repository.FileRepository;
import com.example.CloudStorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final SaveFileMapper saveFileMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public FileResponseDto saveFile(FileDto fileDto, UserEntity user){

        //DTO -> Entity
        UploadedFileEntity file = saveFileMapper.toEntity(fileDto);
        file.setUser(user);

        //Save file
        UploadedFileEntity savedFile = fileRepository.save(file);

        //Entity -> ResponseDto
        FileResponseDto response = saveFileMapper.toResponseDto(savedFile);

        return response;
    }
}
