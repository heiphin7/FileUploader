package com.file.uploader.service.serviceImpl;

import com.file.uploader.configs.SecurityConfig;
import com.file.uploader.dtos.AuthenticationUserDto;
import com.file.uploader.repository.UserRepository;
import com.file.uploader.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> authenticate(AuthenticationUserDto authenticationUserDto) {

        if(checkAllFields(authenticationUserDto)) {
            return new ResponseEntity<>("Заполните все поля!", HttpStatus.BAD_REQUEST);
        }

        if(!userExists(authenticationUserDto)) {
            return new ResponseEntity<>("Имя пользователя не найдено!", HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken
                            (authenticationUserDto.getUsername(), authenticationUserDto.getPassword());

            authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Имя пользователя или пароль неправильный!", HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok("Успешная аутентификация");
    }

    @Override
    public boolean userExists(AuthenticationUserDto authenticationUserDto) {
        return userRepository.findByUsername(authenticationUserDto.getUsername()).isPresent();
    }

    @Override
    public boolean checkAllFields(AuthenticationUserDto authenticationUserDto) {
        return authenticationUserDto.getUsername() == null || authenticationUserDto.getPassword() == null;
    }
}
