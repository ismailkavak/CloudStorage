package com.example.CloudStorage.service;

import com.example.CloudStorage.dto.UserLoginDto;
import com.example.CloudStorage.dto.UserLoginResponseDto;
import com.example.CloudStorage.dto.UserRegisterDto;
import com.example.CloudStorage.dto.UserRegisterResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.exception.InvalidCredentialsException;
import com.example.CloudStorage.exception.UserAlreadyExistsException;
import com.example.CloudStorage.exception.UserNotFoundException;
import com.example.CloudStorage.mapper.UserRegisterMapper;
import com.example.CloudStorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRegisterMapper userRegisterMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
       try{
           Authentication authenticate = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           userLoginDto.getUsername(),
                           userLoginDto.getPassword()
                   ));

           if(!authenticate.isAuthenticated()){
               throw new InvalidCredentialsException("Invalid username or password");
           }else {
                String token = jwtService.generateToken(userLoginDto.getUsername());
               return new UserLoginResponseDto(token);
           }
       } catch (BadCredentialsException e) {
           throw new InvalidCredentialsException("Invalid username or password!");
       }
    }

    public UserEntity getUserById(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public UserEntity getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}