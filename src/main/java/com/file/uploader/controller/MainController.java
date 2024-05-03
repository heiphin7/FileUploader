package com.file.uploader.controller;

import com.file.uploader.entity.File;
import com.file.uploader.entity.User;
import com.file.uploader.repository.FileRepository;
import com.file.uploader.repository.UserRepository;
import com.file.uploader.service.serviceImpl.FileServiceImpl;
import com.file.uploader.utils.TypeChecker;
import com.file.uploader.paths.UploadPaths;
import com.file.uploader.utils.ZipUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainController {

    private final FileServiceImpl fileService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Transactional
    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadNewFile(@RequestParam("file") MultipartFile[] multipartFiles) {
        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElse(null);

        if (user == null) {
            System.out.println("Текущий пользователь не найден");
        }

        try {
            fileService.saveFiles(multipartFiles);
        } catch (Exception e) {
            return new ResponseEntity<>("Прозошла ошибка при сохранении файла: " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok("Файлы успешно сохранены!");
    }

    @GetMapping("/get/file/{id}")
    public ResponseEntity<?> getFileById(@PathVariable Long id) {
        File file = fileRepository.findById(id).orElse(null);

        if(file == null) {
            return new ResponseEntity<>("Файл с " + id + " id не найден!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(file.toString());
    }

    @GetMapping("/user-files/{id}")
    public ResponseEntity<?> getFilesByUserId(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return new ResponseEntity<>("Пользователь с таким id не найден", HttpStatus.BAD_REQUEST);
        }

        List<File> userFiles = fileRepository.findAllByAuthorId(id);

        if(userFiles.size() <= 0) {
            return new ResponseEntity<>("У пользователя нет загруженных файлов", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(userFiles);
    }
}
