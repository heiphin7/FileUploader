package com.file.uploader.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public interface FilesService {

    ResponseEntity<?> saveFiles (MultipartFile multipartFiles);

    String checkType (File file);

    String getPathForFile (MultipartFile multipartFile);

    boolean checkFileExists ();
}
