package com.example.service;

import com.example.dto.UploadedFile;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    String saveImage(UploadedFile part) throws IOException;
    String originalFileName(UploadedFile part);
    Path resolve(String filename) throws IOException;
    void deleteQuietly(String filename);
}
