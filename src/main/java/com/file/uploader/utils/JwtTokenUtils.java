package com.file.uploader.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;


public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime; // 1 hour

}
