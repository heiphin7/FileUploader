package com.file.uploader.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ZipUtils {
    /*

    */

    // Метод для разархивирования
    public static File[] unZip(MultipartFile multipartFile) throws IOException {

        // Данные для дальнейшего разархивирования
        String fileName = multipartFile.getOriginalFilename();
        String path = UploadPaths.ZIP_PATH;
        String fullPath = path + fileName;

        // filePath для того, чтобы туда сохранить multipartFile
        File filePath = new File(fullPath);

        // Ложим в путь саму папка (не ее содержание)
        try {
            multipartFile.transferTo(filePath);
        }catch (IOException e) {
            throw new IOException();
        }

        return filePath.listFiles();
    }
}
