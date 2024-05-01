package com.file.uploader.utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Component;
import org.apache.commons.compress.utils.IOUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.compress.utils.IOUtils.*;


@Component
public class ZipUtils {
    /*   V1
    public static List<File> unZip(String zipFilePath) throws IOException {
        List<File> fileList = new ArrayList<>();
        try (ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new FileInputStream(zipFilePath))) {
            ArchiveEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File outputFile = new File(entry.getName());
                    try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        fileList.add(outputFile);
                    }
                }
            }
        }
        return fileList;
    }
     */

    public static List<File> unZip(String zipFilePath, int numThreads) throws IOException {
        List<File> fileList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    executorService.execute(() -> {
                        try (InputStream inputStream = zipFile.getInputStream(entry)) {
                            File outputFile = new File(entry.getName());
                            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                                IOUtils.copy(inputStream, outputStream);
                                fileList.add(outputFile);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return fileList;
    }

}
