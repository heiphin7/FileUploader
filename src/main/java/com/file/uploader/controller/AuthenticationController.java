package com.file.uploader.controller;

import com.file.uploader.dtos.AuthenticationUserDto;
import com.file.uploader.service.serviceImpl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationUserDto authenticationUserDto) {
        return authenticationService.authenticate(authenticationUserDto);
    }
}
