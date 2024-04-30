package com.file.uploader.controller;

import com.file.uploader.utils.TypeChecker;
import com.file.uploader.utils.UploadPaths;
import com.file.uploader.utils.ZipUtils;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class MainController {

    /*
        Данный метод должен быть Transactional, так как не должно быть такого, что
        часть файлов сохраниться, а остальная - нет
    */

    @Transactional
    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadNewFile(@RequestParam("file") MultipartFile[] multipartFiles) {
        // Используя цикл, перебираем каждый файл

        for(MultipartFile multipartFile: multipartFiles) {

            // Вытаскиваем такие значения как: Тип файла и Полное название файла
            String typeOfFile = multipartFile.getContentType();
            String fullNameOfFile = multipartFile.getOriginalFilename();

            // Проверка на null, чтобы пользователь не отправлял пустые запросы
            if(typeOfFile == null) {
                return new ResponseEntity<>("Вы не загрузили файл!", HttpStatus.BAD_REQUEST);
            }

            // Используя метод getPathByType определяем путь,куда можно сохранить файл
            String pathToSave = TypeChecker.getPathByType(typeOfFile);

            // Если файл с таким названием и в такой же директории существует
            if(new File(pathToSave + fullNameOfFile).exists()) {
                return new ResponseEntity<>
                        ("Такой файл уже существует, выберите дргуое название", HttpStatus.BAD_REQUEST);
            }

             // После того, как мы определили, куда будет файл сохраняться мы должны сохранить сам файл
             String fileName = multipartFile.getOriginalFilename();
             String path = pathToSave;
             File pathFile = new File(path);

             // Если указанная в pathFile путь или папка не существует, тогда создаем
             if(!pathFile.exists()){
                 pathFile.mkdir();
             }

             pathFile = new File(path + fileName);

             try {
                multipartFile.transferTo(pathFile);
             } catch (IOException e) {
                 System.out.println(e);
                 return new ResponseEntity<>
                         ("Ошибка при попытке сохранить файл", HttpStatus.INTERNAL_SERVER_ERROR);
             }


             // Обработка ZIP-файла, то есть распаковка его файлов
            if (typeOfFile.equals("application/zip")) {
                String fullPath = pathToSave + fileName;
                List<File> filesList;


                try {
                    filesList = ZipUtils.unZip(fullPath);
                } catch (IOException e) {
                    System.out.println(e);
                    return new ResponseEntity<>("Ошибка при распаковке архива", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                // Цикл для перебора файлов из zip-a
                for (File file: filesList) {
                    String name = file.getName();
                    String fileExtension = ""; // Расширение файла

                    // Получение расширения файла
                    int dotIndex = name.lastIndexOf('.');
                    if (dotIndex > 0) {
                        fileExtension = name.substring(dotIndex + 1);
                        fileExtension.toLowerCase();
                    }

                    // Путь для сохранения
                    String destinationPath = "";

                    System.out.println(destinationPath);

                    if(fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
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
                        System.out.println("Ошибка при перемещении файла " + name);
                    }

                }
            }

        }

        return ResponseEntity.ok("Файлы успешно сохранены!");
    }

}
