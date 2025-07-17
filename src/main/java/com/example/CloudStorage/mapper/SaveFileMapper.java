package com.example.CloudStorage.mapper;

import com.example.CloudStorage.dto.UploadedFileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UploadedFileEntity;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaveFileMapper {
    //DTO -> Entity
    UploadedFileEntity toEntity(MultipartFile multipartFile);

    //Entity -> DTO
    UploadedFileDto toUploadedFileDto(UploadedFileEntity fileEntity);

    //Entity -> Response DTO List
    List<FileResponseDto> toDtoList(List<UploadedFileEntity> entities);
}
