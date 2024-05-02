package com.file.uploader.service.serviceImpl;

import com.file.uploader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    // final type for DI (Dependency Injection)

    private final UserRepository userRepository;


}
