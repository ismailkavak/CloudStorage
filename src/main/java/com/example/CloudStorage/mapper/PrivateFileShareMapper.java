package com.example.CloudStorage.mapper;


import com.example.CloudStorage.dto.PrivateFileShareRequestDto;
import com.example.CloudStorage.dto.PrivateFileShareResponseDto;
import com.example.CloudStorage.entity.PrivateFileShareEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrivateFileShareMapper {
    //DTO -> Entity
    PrivateFileShareEntity toEntity(PrivateFileShareRequestDto requestDto);

    //Entity -> DTO
    PrivateFileShareResponseDto toResponseDto(PrivateFileShareEntity shareEntity);

    //Entity -> DTO LIST
    List<PrivateFileShareResponseDto> toResponseDtoList(List<PrivateFileShareEntity> shareEntities);
}
