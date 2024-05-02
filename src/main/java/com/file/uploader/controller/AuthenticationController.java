package com.file.uploader.controller;

import com.file.uploader.dtos.AuthenticationUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationUserDto authenticationUserDto) {

        if(authenticationUserDto == null) {
            return new ResponseEntity<>("null", HttpStatus.BAD_REQUEST);
        }

        System.out.println(authenticationUserDto);

        try {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            authenticationUserDto.getUsername(),
                            authenticationUserDto.getPassword(),
                            null
                    )
            );

            // Возвращаем успешный ответ
            return ResponseEntity.ok("Успешная аутентификация");
        } catch (Exception e) {
            // Возвращаем ошибку при неудачной аутентификации
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
