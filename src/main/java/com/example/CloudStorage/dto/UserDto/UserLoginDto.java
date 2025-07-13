package com.example.CloudStorage.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank(message = "You should enter a username!")
    @Size(min = 4, message = "Password must be at least 4 chars.")
    private String username;

    @NotBlank(message = "You should enter a password!")
    @Size(min = 6, message = "Password must be at least 6 chars.")
    private String password;
}
