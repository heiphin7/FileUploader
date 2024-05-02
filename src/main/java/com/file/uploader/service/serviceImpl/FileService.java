package com.file.uploader.service.serviceImpl;

import com.file.uploader.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class FileService implements FilesService {

    // Главный метод для сохранения файлов
    @Override
    public ResponseEntity<?> saveFiles(MultipartFile multipartFiles) {
        return null;
    }


    @Override
    public boolean checkFileExists() {
        return false;
    }

    @Override
    public String getPathForFile(MultipartFile multipartFile) {
        return null;
    }



    @Override
    public String checkType(File file) {
        return null;
    }
}
