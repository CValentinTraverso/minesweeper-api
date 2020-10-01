package com.example.minesweep.service;

import com.example.minesweep.function.AuthenticatedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final String secretKey;
    private final Long expiration;
    private final ObjectMapper objectMapper;
    private final String saltKey;

    public AuthService(@Value("${software.security.jwt.secret-key}") String secretKey, @Value("${software.security.salt.key}") String saltKey, @Value("${software.security.jwt.expiration-in-minutes}") Long expirationInMinutes,
                       ObjectMapper objectMapper) {
        this.secretKey = secretKey;
        this.expiration = 1000L * 60L * expirationInMinutes;
        this.objectMapper = objectMapper;
        this.saltKey = saltKey;
    }

    public String getToken(Long userId) {
        return this.getToken(userId, "");
    }

    public String getToken(Long userId, String role) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(role);
        return Jwts.builder()
                .claim("user", AuthenticatedUser.builder().userId(userId).build())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
    }

    public AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            String claimAsString = objectMapper.writeValueAsString(authentication.getPrincipal());
            return objectMapper.readValue(claimAsString, AuthenticatedUser.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get user from token");
        }
    }

    public String createRefreshToken(Long id, String email, String password) {
        return DigestUtils.sha256Hex(id + email + password + saltKey);
    }
}
