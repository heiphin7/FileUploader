package com.file.uploader.controller;

import com.file.uploader.dtos.RegistrationDto;
import com.file.uploader.service.RegistrationService;
import com.file.uploader.service.serviceImpl.RegistrationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationServiceImpl registrationService;

    @PostMapping("/registration")
    public ResponseEntity<?> registrateNewUser(@RequestBody RegistrationDto registrationDto) {
        return registrationService.saveUser(registrationDto);
    }
}
