package com.file.uploader.threads;


import com.file.uploader.utils.UploadPaths;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class FileProcessor implements Runnable{
    private List<File> fileList;

    public FileProcessor (List<File> fileList) {
        this.fileList = fileList;
    }

    @Override
    public void run() {

        for (File file: fileList) {
            String name = file.getName();
            String fileExtension = ""; // Расширение файла

            // Получение расширения файла
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = name.substring(dotIndex + 1);
            }

            // Путь для сохранения
            String destinationPath = "";

            System.out.println(fileExtension.toLowerCase());

            destinationPath = switch (fileExtension.toLowerCase()) {
                case "jpeg", "jpg" -> UploadPaths.IMAGES_JPEG_PATH;
                case "png" -> UploadPaths.IMAGES_PNG_PATH;
                case "pdf" -> UploadPaths.PDF_PATH;
                case "json" -> UploadPaths.JSON_PATH;
                case "txt" -> UploadPaths.TEXTS_PATH;
                default -> UploadPaths.OTHERS_PATH;
            };

            // Далее, в зависимости от файла мы должны закинуть его в одну из директорий
            // Создание файла для перемещения
            File destinationFile = new File(destinationPath + name);

            // Перемещение файла
            try {
                Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(file.toPath());
                System.out.println("Файл " + name + " успешно перемещен в " + destinationPath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка при перемещении файла " + name);
            }
        }

    }
}
