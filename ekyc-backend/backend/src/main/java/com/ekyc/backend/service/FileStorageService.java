package com.ekyc.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String store(MultipartFile file, String subFolder) {
        try {
            Path dirPath = Paths.get(uploadDir, subFolder);
            Files.createDirectories(dirPath);

            String extension = getExtension(file.getOriginalFilename());
            // Dung UUID lam ten file - tranh trung ten va khong lo thong tin tu ten file goc
            String filename = UUID.randomUUID() + extension;
            Path targetPath = dirPath.resolve(filename);

            Files.copy(file.getInputStream(), targetPath);

            return targetPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Khong the luu file: " + e.getMessage(), e);
        }
    }

    public byte[] readBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Khong doc duoc file: " + path, e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf("."));
    }
}