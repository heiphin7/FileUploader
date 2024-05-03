package com.file.uploader.controller;

import com.file.uploader.dtos.AuthenticationUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationUserDto authenticationUserDto) {

        if(authenticationUserDto == null) {
            return new ResponseEntity<>("Введите имя пользователя и пароль", HttpStatus.BAD_REQUEST);
        }

        try {
            GrantedAuthority defaultRole = new SimpleGrantedAuthority("ROLE_USER"); // default Role

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationUserDto.getUsername(),
                            authenticationUserDto.getPassword(),
                            Collections.singleton(defaultRole)
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println(SecurityContextHolder.getContext());

            // Возвращаем успешный ответ
            return ResponseEntity.ok("Успешная аутентификация");
        } catch (Exception e) {
            // Возвращаем ошибку при неудачной аутентификации
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/secured")
    public String secured() {
        return "Hello from secured endpoint";
    }
}
