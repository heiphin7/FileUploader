package com.file.uploader.service.serviceImpl;

import com.file.uploader.dtos.AuthenticationUserDto;
import com.file.uploader.repository.UserRepository;
import com.file.uploader.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(AuthenticationUserDto authenticationUserDto) {

        if (checkAllFields(authenticationUserDto)) {
            throw new AuthenticationServiceException("Все поля должны быть заполнены!");
        }

        if (!userExists(authenticationUserDto)) {
            throw new AuthenticationServiceException("Имя пользователя не найдено!");
        }

        try {
            return new UsernamePasswordAuthenticationToken(authenticationUserDto.getUsername(), authenticationUserDto.getPassword());
        } catch (BadCredentialsException e) {
            throw new AuthenticationServiceException("Неправильный пароль!");
        }
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
