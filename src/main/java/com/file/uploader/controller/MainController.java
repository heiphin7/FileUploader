package com.file.uploader.controller;

import com.file.uploader.utils.UploadPaths;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


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
            String typeOfFile = multipartFile.getContentType();
            String pathToSave = "";

            // Если тип файла - zip, тогда мы должны обработать каждый файл в данном zip
            if(typeOfFile.equals("application/zip")) {

            }

            // Если он - изображение
            if(typeOfFile.startsWith("image")) {
                /*
                    В зависимости от расширения изображения, выбираем путь для сохранения
                    PNG & JPEG
                */
                if(typeOfFile.equals("image/jpeg")) {
                    pathToSave = UploadPaths.IMAGES_JPEG_PATH;
                } else if (typeOfFile.equals("image/png")) {
                    pathToSave = UploadPaths.IMAGES_PNG_PATH;
                }
            }

            /*
                Типы файлов:
                application/pdf
                application/json
                application/xml
                application/octet-stream - // Остальные типы
            */
             else if(typeOfFile.startsWith("application")) {

                if(typeOfFile.equals("application/pdf")) {
                    pathToSave = UploadPaths.PDF_PATH;
                } else if (typeOfFile.equals("application/json")) {
                    pathToSave = UploadPaths.JSON_PATH;
                } else if (typeOfFile.equals("application/xml")) {
                    pathToSave = UploadPaths.XML_PATH;
                } else { // Other types
                    pathToSave = UploadPaths.OTHERS_PATH;
                }
            }

            // Тип - текст
             else if(typeOfFile.equals("text/plain")) {
                pathToSave = UploadPaths.TEXTS_PATH;
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
        }

        return ResponseEntity.ok("Файлы успешно сохранены!");
    }

}
