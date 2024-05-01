package com.file.uploader.threads;

import com.file.uploader.utils.UploadPaths;

import java.io.File;
import java.util.List;

public class FileProcessor implements Runnable {
    private List<File> fileList;

    public FileProcessor(List<File> fileList) {
        this.fileList = fileList;
    }

    @Override
    public void run() {
        // Цикл для перебора файлов из zip-a
        for (File file: fileList) {
            String name = file.getName();
            String fileExtension = ""; // Расширение файла

            // Получение расширения файла
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = name.substring(dotIndex + 1);
                fileExtension = fileExtension.toLowerCase();
            }

            System.out.println("File Name: " + name + ", Extension: " + fileExtension);

            // Путь для сохранения
            String destinationPath = "";

            System.out.println(destinationPath);

            if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
                destinationPath = UploadPaths.IMAGES_JPEG_PATH;
            } else if (fileExtension.equals("png")) {
                destinationPath = UploadPaths.IMAGES_PNG_PATH;
            } else if (fileExtension.equals("pdf")) {
                destinationPath = UploadPaths.PDF_PATH;
            } else if(fileExtension.equals("json")) {
                destinationPath = UploadPaths.JSON_PATH;
            } else if (fileExtension.equals("txt")) {
                destinationPath = UploadPaths.TEXTS_PATH;
            }

            // Далее, в зависимости от файла мы должны закинуть его в одну из директорий
            // Создание файла для перемещения
            File destinationFile = new File(destinationPath + name);

            // Перемещение файла
            if (file.renameTo(destinationFile)) {
                // Успешно перемещен
                System.out.println("Файл " + name + " успешно перемещен в " + destinationPath);
            } else {
                // Возникла ошибка при перемещении файла
                System.out.println("Ошибка при перемещении файла: " + name);
            }

        }
    }
}
