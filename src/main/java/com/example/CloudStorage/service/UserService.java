package com.example.CloudStorage.service;

import com.example.CloudStorage.dto.UserLoginDto;
import com.example.CloudStorage.dto.UserLoginResponseDto;
import com.example.CloudStorage.dto.UserRegisterDto;
import com.example.CloudStorage.dto.UserRegisterResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.exception.InvalidCredentialsException;
import com.example.CloudStorage.exception.UserAlreadyExistsException;
import com.example.CloudStorage.exception.UserNotFoundException;
import com.example.CloudStorage.exception.WrongPasswordException;
import com.example.CloudStorage.mapper.UserRegisterMapper;
import com.example.CloudStorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRegisterMapper userRegisterMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserRegisterResponseDto saveUser(UserRegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("This user already exist!");
        }
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        //DTO -> Entity
        UserEntity user = userRegisterMapper.toEntity(registerDto);

        //Save Entity
        UserEntity savedUser = userRepository.save(user);

        //Entity -> ResponseDto
        UserRegisterResponseDto registerResponseDto = userRegisterMapper.toResponseDto(savedUser);
        registerResponseDto.setMessage("Registration successful!");

        return registerResponseDto;
    }

    public UserLoginResponseDto loginUser(UserLoginDto userLoginDto){
        UserEntity user = userRepository.findByUsername(userLoginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        //If login is non-success
        if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid username or password!");
        }
        UserLoginResponseDto loginResponse = new UserLoginResponseDto();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setMessage("Login successful!");
        return loginResponse;
    }
}
