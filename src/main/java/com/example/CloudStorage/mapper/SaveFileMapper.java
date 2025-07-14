package com.example.CloudStorage.mapper;

import com.example.CloudStorage.dto.FileDto;
import com.example.CloudStorage.dto.FileResponseDto;
import com.example.CloudStorage.entity.UploadedFileEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaveFileMapper {
    //DTO -> Entity
    UploadedFileEntity toEntity(FileDto fileDto);

    //Entity -> DTO
    FileResponseDto toResponseDto(UploadedFileEntity fileEntity);

    //Entity -> Response DTO List
    List<FileResponseDto> toDtoList(List<UploadedFileEntity> entities);
}
