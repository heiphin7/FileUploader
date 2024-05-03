package com.file.uploader.service.serviceImpl;

import com.file.uploader.dtos.RegistrationDto;
import com.file.uploader.entity.Role;
import com.file.uploader.entity.User;
import com.file.uploader.repository.RoleRepository;
import com.file.uploader.repository.UserRepository;
import com.file.uploader.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> saveUser(RegistrationDto registrationDto) {

        if(checkAllFields(registrationDto)) {
            return new ResponseEntity<>("Все поля должны быть заполнены!", HttpStatus.BAD_REQUEST);
        }

        if (checkPasswords(registrationDto)) {
            return new ResponseEntity<>("Пароль и его подтверждение должны быть совпадать!", HttpStatus.BAD_REQUEST);
        }

        if(usernameExists(registrationDto)) {
            return new ResponseEntity<>("Имя пользователя занято!", HttpStatus.BAD_REQUEST);
        }

        if(registrationDto.getUsername().length() < 6) {
            return new ResponseEntity<>("Имя пользователя должно быть больше 6-ти символов", HttpStatus.BAD_REQUEST);
        }

        if(registrationDto.getPassword().length() < 8 || registrationDto.getConfirmPassword().length() < 8) {
            return new ResponseEntity<>("Пароль и его подтверждение должно быть больше 8-ми смиволов!", HttpStatus.BAD_REQUEST);
        }

        User user = createNewUser(registrationDto);
        userRepository.save(user);

        return ResponseEntity.ok("Пользователь успешно сохранен, теперь пройдите аутентификацию!");
    }

    @Override
    public boolean checkPasswords(RegistrationDto registrationDto) {
        return !registrationDto.getPassword().equals(registrationDto.getConfirmPassword());
    }

    @Override
    public boolean usernameExists(RegistrationDto registrationDto) {
        Optional<User> optionalUser = userRepository.findByUsername(registrationDto.getUsername());

        return optionalUser.isPresent();
    }

    @Override
    public boolean checkAllFields(RegistrationDto registrationDto) {
        return registrationDto.getUsername() == null || registrationDto.getPassword() == null || registrationDto.getConfirmPassword() == null;
    }

    @Override
    public User createNewUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registrationDto.getPassword()));
        user.setNumberOfFiles(0L);  // Количество файлов изначально равно нулю

        Role role = roleRepository.findByName("ROLE_USER").orElse(new Role());

        // Если в базе данных нету роли user-a, тогда создаем
        if(role.getName() == null) {
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }

        user.setRoles(List.of(role));

        return user;
    }
}
