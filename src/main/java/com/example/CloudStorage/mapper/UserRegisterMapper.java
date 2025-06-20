package com.example.CloudStorage.mapper;

import com.example.CloudStorage.dto.UserRegisterDto;
import com.example.CloudStorage.dto.UserRegisterResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import org.apache.catalina.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
    //DTO -> Entity
    UserEntity toEntity(UserRegisterDto userRegisterDto);

    //Entity -> DTO
    UserRegisterResponseDto toResponseDto(UserEntity user);
}
