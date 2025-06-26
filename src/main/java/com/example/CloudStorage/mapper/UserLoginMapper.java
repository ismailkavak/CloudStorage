package com.example.CloudStorage.mapper;

import com.example.CloudStorage.dto.UserLoginDto;
import com.example.CloudStorage.dto.UserLoginResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    // DTO -> Entity
    UserEntity toEntity(UserLoginDto userLoginDto);

    // Entity -> DTO
    UserLoginResponseDto toResponseDto(UserEntity user);
}