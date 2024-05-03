package com.file.uploader.service.serviceImpl;

import com.file.uploader.dtos.AuthenticationUserDto;
import com.file.uploader.repository.UserRepository;
import com.file.uploader.service.AuthenticationService;
import com.file.uploader.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public ResponseEntity<?> authenticate(AuthenticationUserDto authenticationUserDto) {

        if(checkAllFields(authenticationUserDto)) {
            return new ResponseEntity<>("Все поля должны быть заполнены", HttpStatus.BAD_REQUEST);
        }

        if(!userExists(authenticationUserDto)) {
            return new ResponseEntity<>("Имя пользователя не найдено!", HttpStatus.BAD_REQUEST);
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationUserDto.getUsername(),
                        authenticationUserDto.getPassword()
                )
        );

        // For JWT token
        UserDetails userDetails = userService.
                loadUserByUsername(authenticationUserDto.getUsername());

        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(
                "Успешная аутентификация \n" +
                   "Jwt: \n" + token
        );
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
