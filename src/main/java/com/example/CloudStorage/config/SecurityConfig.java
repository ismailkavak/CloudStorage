package com.example.CloudStorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF'yi devre dışı bırak (REST API için)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/register", "/login").permitAll() // Bu endpoint'ler herkese açık
                        .anyRequest().authenticated() // Diğer tüm endpoint'ler authentication gerektirir
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic auth'u devre dışı bırak
                .formLogin(form -> form.disable()); // Form login'i devre dışı bırak

        return http.build();
    }
}
