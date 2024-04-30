package com.file.uploader.utils;

import org.springframework.stereotype.Component;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


@Component
public class ZipUtils {
    /*

    */

    // Метод для разархивирования и возвращении всех его файлов
    public static List<File> unZip(String zipFilePath) throws IOException {
        List<File> fileList = new ArrayList<>();
        try (ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new FileInputStream(zipFilePath))) {
            ArchiveEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File outputFile = new File(entry.getName());
                    try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                        IOUtils.copy(zipInputStream, outputStream);
                    }
                    fileList.add(outputFile);
                }
            }
        }
        return fileList;
    }
}
