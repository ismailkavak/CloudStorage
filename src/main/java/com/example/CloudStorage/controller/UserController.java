package com.example.CloudStorage.controller;

import com.example.CloudStorage.dto.UserRegisterDto;
import com.example.CloudStorage.dto.UserRegisterResponseDto;
import com.example.CloudStorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    public UserRegisterResponseDto register(@RequestBody UserRegisterDto userRegisterDto){
        return userService.saveUser(userRegisterDto);
    }
}
