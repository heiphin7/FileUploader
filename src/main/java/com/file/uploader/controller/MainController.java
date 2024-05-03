package com.file.uploader.controller;

import com.file.uploader.service.serviceImpl.FileServiceImpl;
import com.file.uploader.utils.TypeChecker;
import com.file.uploader.paths.UploadPaths;
import com.file.uploader.utils.ZipUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainController {

    // Используем fileService для DI (Внедрение зависимостей)
    private final FileServiceImpl fileService;


    /*
        Данный метод должен быть Transactional, так как не должно быть такого, что
        часть файлов сохраниться, а остальная - нет
    */

    @Transactional
    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadNewFile(@RequestParam("file") MultipartFile[] multipartFiles) {

        // TODO вынести реализацию в отдельные файлы (service, serviceImpl)




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
                List<byte[]> filesContentList;

                try {
                    filesContentList = ZipUtils.unZip(fullPath);
                } catch (IOException e) {
                    return new ResponseEntity<>("Ошибка при разархивировании файла", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                if (filesContentList.size() > 1) {
                    for (byte[] fileContent : filesContentList) {
                        // Необходимые параметры для сохранения файла в разные директории
                        String fileExtension = ""; // Расширение файла
                        String destinationPath = ""; // Путь для сохранения

                        // Получение расширения файла (можно использовать другие методы для определения типа файла)
                        // В этом примере предполагается, что расширение содержится в имени файла
                        String fileName1 = ""; // Имя файла
                        int dotIndex = fileName1.lastIndexOf('.');
                        if (dotIndex > 0) {
                            fileExtension = fileName1.substring(dotIndex + 1);
                            fileExtension = fileExtension.toLowerCase();
                        }

                        // Определяем путь для сохранения
                        if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
                            destinationPath = UploadPaths.IMAGES_JPEG_PATH;
                        } else if (fileExtension.equals("png")) {
                            destinationPath = UploadPaths.IMAGES_PNG_PATH;
                        } else if (fileExtension.equals("pdf")) {
                            destinationPath = UploadPaths.PDF_PATH;
                        } else if (fileExtension.equals("json")) {
                            destinationPath = UploadPaths.JSON_PATH;
                        } else if (fileExtension.equals("txt")) {
                            destinationPath = UploadPaths.TEXTS_PATH;
                        }

                        // Очистка имени файла от недопустимых символов
                        // Для определения имени файла вы можете использовать другие методы, в зависимости от вашей логики
                        String cleanedFileName = ""; // Имя файла
                        String destinationFilePath = destinationPath + cleanedFileName;

                        // Сохранение содержимого файла в указанное место
                        try (FileOutputStream outputStream = new FileOutputStream(destinationFilePath)) {
                            outputStream.write(fileContent);
                            System.out.println("Файл успешно сохранен в " + destinationFilePath);
                        } catch (IOException e) {
                            System.out.println("Ошибка при сохранении файла: " + destinationFilePath);
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        return ResponseEntity.ok("Файлы успешно сохранены!");
    }
}
