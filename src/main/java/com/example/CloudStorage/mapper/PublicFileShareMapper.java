package com.example.CloudStorage.mapper;

import com.example.CloudStorage.dto.PublicFileShareDto;
import com.example.CloudStorage.entity.PublicFileShareEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicFileShareMapper {
    //Entity -> DTO
    PublicFileShareDto toPublicFileShareDto(PublicFileShareEntity publicFileShareEntity);
}