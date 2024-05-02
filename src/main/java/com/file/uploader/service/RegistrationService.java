package com.file.uploader.service;

import com.file.uploader.dtos.RegistrationDto;
import com.file.uploader.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface RegistrationService {

    // Ну и главный метод - сохранение пользователя
    ResponseEntity<?> saveUser (RegistrationDto registrationDto);

    // Совпадает ли пароль с его подтверждением
    boolean checkPasswords (RegistrationDto registrationDto);

    // Не занято ли имя пользователя
    boolean usernameExists (RegistrationDto registrationDto);

    // Проверка всех полей, не пустые ли они
    boolean checkAllFields (RegistrationDto registrationDto);

    User createNewUser(RegistrationDto registrationDto);
}
