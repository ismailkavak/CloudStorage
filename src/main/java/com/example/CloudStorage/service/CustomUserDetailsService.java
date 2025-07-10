package com.example.CloudStorage.service;

import com.example.CloudStorage.UserDetails.CustomUserDetails;
import com.example.CloudStorage.entity.UserEntity;
import com.example.CloudStorage.exception.UserNotFoundException;
import com.example.CloudStorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        if (Objects.isNull(user)){
            throw new UserNotFoundException("User not found!");
        }
        return new CustomUserDetails(user);
    }
}
