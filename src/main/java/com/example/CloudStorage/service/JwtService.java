package com.example.CloudStorage.service;

import com.example.CloudStorage.dto.UserLoginDto;
import com.example.CloudStorage.dto.UserLoginResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final String jwtSecret;

    public JwtService
            (@Value("${JWT_SECRET}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }


    public String generateToken(String username) {
        return Jwts
                .builder()
                .subject(username)
                .issuer("poyraz")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60*10*10000))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey(){
        byte[] decode = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decode);
    }

}
