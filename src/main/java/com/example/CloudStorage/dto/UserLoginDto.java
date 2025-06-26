package com.example.CloudStorage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank(message = "You should enter a username!")
    @Size(min = 4, message = "Password must be at least 4 chars.")
    String username;

    @NotBlank(message = "You should enter a password!")
    @Size(min = 6, message = "Password must be at least 6 chars.")
    private String password;
}
