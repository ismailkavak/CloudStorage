package com.example.CloudStorage.service;

import com.example.CloudStorage.dto.FileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UploadedFileEntity;
import com.example.CloudStorage.mapper.SaveFileMapper;
import com.example.CloudStorage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final SaveFileMapper saveFileMapper;

    public FileResponseDto saveFile(FileDto fileDto){
        //DTO -> Entity
        UploadedFileEntity file = saveFileMapper.toEntity(fileDto);

        //Save file
        UploadedFileEntity savedFile = fileRepository.save(file);

        //Entity -> ResponseDto
        FileResponseDto response = saveFileMapper.toResponseDto(savedFile);

        return response;
    }
}
