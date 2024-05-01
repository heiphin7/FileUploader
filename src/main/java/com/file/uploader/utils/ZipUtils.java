package com.file.uploader.utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Component
public class ZipUtils {

    public static List<File> unZip(String zipFilePath) throws IOException {
        List<File> fileList = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            ExecutorService executor = Executors.newFixedThreadPool(4); // 4 потоков
            List<Future<File>> futures = new ArrayList<>();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    Future<File> future = executor.submit(() -> {
                        try (InputStream inputStream = zipFile.getInputStream(entry)) {
                            File outputFile = new File(entry.getName());
                            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                                byte[] buffer = new byte[32768];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                            }
                            return outputFile;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
                    futures.add(future);
                }
            }

            for (Future<File> future : futures) {
                try {
                    File outputFile = future.get();
                    if (outputFile != null) {
                        fileList.add(outputFile);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileList;
    }
}
