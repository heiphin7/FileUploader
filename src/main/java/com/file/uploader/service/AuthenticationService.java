package com.file.uploader.service;

import com.file.uploader.dtos.AuthenticationUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationService {

    ResponseEntity<?> authenticate(AuthenticationUserDto authenticationUserDto);
    boolean userExists(AuthenticationUserDto authenticationUserDto);
    boolean checkAllFields(AuthenticationUserDto authenticationUserDto);
}
