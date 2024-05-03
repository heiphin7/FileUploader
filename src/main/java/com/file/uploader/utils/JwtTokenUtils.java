package com.file.uploader.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime; // 1 hour

    public String generateToken(UserDetails user) {

        Map<String, Object> claims = new HashMap<>();

        List<String> rolesList = user.getAuthorities().stream().map(
                GrantedAuthority::getAuthority
        ).collect(Collectors.toList());

        claims.put("roles", rolesList);

        Date issuedAt = new Date(); // Дата подписания
        Date expiredDate = new Date(issuedAt.getTime() + lifetime.toMillis()); // Дата истечения токена

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret) // Используем Base64 для кодирования секретного ключа
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject(); // Subject is Username
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }


    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret) // Используем Base64 для кодированного секретного ключа
                .parseClaimsJws(token).getBody();
    }
}
