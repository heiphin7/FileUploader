package com.file.uploader.utils;

import org.springframework.stereotype.Component;

@Component
public class TypeChecker {
    /*
        Данный класс содержит методы, которые помогают определить тип файла
        Кроме того, он еще возвращает путь, в который нужно сохранить нужный файл
     */

    public static String getPathByType(String typeOfFile) {
        String pathToSave = "";

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
            } else if (typeOfFile.equals("appication/zip")) {
                pathToSave = UploadPaths.ZIP_PATH;
            }else { // Other types
                pathToSave = UploadPaths.OTHERS_PATH;
            }
        }

        // Тип - текст
        else if(typeOfFile.equals("text/plain")) {
            pathToSave = UploadPaths.TEXTS_PATH;
        }

        return pathToSave;
    }
}
