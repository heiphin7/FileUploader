package com.file.uploader.controller;

import com.file.uploader.utils.TypeChecker;
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
            String typeOfFile = multipartFile.getContentType();
            String fullNameOfFile = multipartFile.getOriginalFilename();

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
                 System.out.println(multipartFile.getResource());
             } catch (IOException e) {
                 System.out.println(e);
                 return new ResponseEntity<>
                         ("Ошибка при попытке сохранить файл", HttpStatus.INTERNAL_SERVER_ERROR);
             }
        }

        return ResponseEntity.ok("Файлы успешно сохранены!");
    }

}
