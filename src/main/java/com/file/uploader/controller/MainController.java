package com.file.uploader.controller;

import com.file.uploader.threads.FileProcessor;
import com.file.uploader.utils.TypeChecker;
import com.file.uploader.utils.UploadPaths;
import com.file.uploader.utils.ZipUtils;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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


                try{
                    filesList = ZipUtils.unZip(fullPath);
                }catch (IOException e){
                    return new ResponseEntity<>("Ошибка при разархивировании файла", HttpStatus.INTERNAL_SERVER_ERROR);
                }



                if(filesList.size() > 1) {

                    for (File file: filesList) {
                        // Необходимые параметры для сохранения файла в разные директории
                        String FullName = file.getName();
                        String fileExtension = ""; // Расширение файла
                        String destinationPath = ""; // Путь для сохранения

                        // Получение расширения файла
                        int dotIndex = FullName.lastIndexOf('.');
                        if (dotIndex > 0) {
                            fileExtension = FullName.substring(dotIndex + 1);
                            fileExtension = fileExtension.toLowerCase();
                        }

                        // Определяем путь для сохранения
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

                        // Очистка имени файла от недопустимых символов
                        String cleanedFileName = FullName.replaceAll("[^a-zA-Z0-9.-]", "_");

                        // Создание нового файла с очищенным именем
                        File destinationFile = new File(destinationPath + File.separator + cleanedFileName);


                        // Перемещение файла
                        if (file.renameTo(destinationFile)) {
                            // Успешно перемещен
                            System.out.println("Файл " + FullName + " успешно перемещен в " + destinationPath);
                        } else {
                            // Возникла ошибка при перемещении файла
                            System.out.println("Ошибка при перемещении файла: " + FullName);
                        }
                    }
                }

                /*
                System.out.println(filesList.size());

                List<File> firstPart = filesList.subList(0, filesList.size() / 2);
                List<File> secondPart = filesList.subList(filesList.size() / 2, filesList.size());

                FileProcessor firstFileProcessor = new FileProcessor(firstPart);
                FileProcessor secondFileProcessor = new FileProcessor(secondPart);

                Thread firstThread = new Thread(firstFileProcessor);
                Thread secondThread = new Thread(secondFileProcessor);

                firstThread.start();
                secondThread.start();

                try {
                    firstThread.join();
                    secondThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
            }
        }

        return ResponseEntity.ok("Файлы успешно сохранены!");
    }

}
