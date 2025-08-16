package com.example.CloudStorage.controller;

import com.example.CloudStorage.dto.UserLoginDto;
import com.example.CloudStorage.dto.UserLoginResponseDto;
import com.example.CloudStorage.dto.UserRegisterDto;
import com.example.CloudStorage.dto.UserRegisterResponseDto;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getuser/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String id){
        UserEntity response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@RequestBody @Valid UserRegisterDto userRegisterDto){
        UserRegisterResponseDto response = userService.saveUser(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginDto userLoginDto){
        UserLoginResponseDto response = userService.loginUser(userLoginDto);
        return ResponseEntity.ok(response);
    }
}