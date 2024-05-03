package com.file.uploader.utils;

import com.file.uploader.paths.UploadPaths;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TypeChecker {
    /*
        Данный класс содержит методы, которые помогают определить тип файла
        Кроме того, он еще возвращает путь, в который нужно сохранить нужный файл

        Types of multipart (3):
            -- 1
            image/jpeg
            image/png

            -- 2
            application/pfd
            application/json
            application/xml
            application/zip

            -- 3
            text/plain


            -- Others
            application/other
    */

    public static String getPathByType(String typeOfFile) {
        String pathToSave = "";

        // 1 - Type
        if(typeOfFile.startsWith("image")) {
            if(typeOfFile.equals("image/jpeg")) {
                pathToSave = UploadPaths.IMAGES_JPEG_PATH;
            } else if (typeOfFile.equals("image/png")) {
                pathToSave = UploadPaths.IMAGES_PNG_PATH;
            }
        }
         // 2 - Type
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
         // 3 - Type
          else if(typeOfFile.equals("text/plain")) {
            pathToSave = UploadPaths.TEXTS_PATH;
        }

        return pathToSave;
    }

    // Метод для получения типа по multipartFile
    public static String getTypeOfFile (MultipartFile multipartFile) {
        String typeFile = multipartFile.getContentType();

        if(typeFile == null) {
            return null;
        }

        switch (typeFile) {

            case "image/jpeg":
            case "image/jpg":
                return "jpeg";

            case "image/png":
                return "png";

            case "application/pdf":
                return "pdf";

            case "application/json":
                return "json";

            case "application/msword":
                return "doc";

            default:
                return "other";
        }
    }

}
