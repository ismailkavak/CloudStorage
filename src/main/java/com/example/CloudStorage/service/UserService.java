package com.example.CloudStorage.service;

import com.example.CloudStorage.dto.UserRegisterDto;
import com.example.CloudStorage.dto.UserRegisterResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.exception.UserAlreadyExistsException;
import com.example.CloudStorage.mapper.UserRegisterMapper;
import com.example.CloudStorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRegisterMapper userRegisterMapper;


    public UserRegisterResponseDto saveUser(UserRegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("This user already exist!");
        }
        //DTO -> Entity
        UserEntity user = userRegisterMapper.toEntity(registerDto);

        //Save Entity
        UserEntity savedUser = userRepository.save(user);

        //Entity -> ResponseDto
        UserRegisterResponseDto registerResponseDto = userRegisterMapper.toResponseDto(savedUser);
        registerResponseDto.setMessage("Registration successful!");

        return registerResponseDto;
    }
}
