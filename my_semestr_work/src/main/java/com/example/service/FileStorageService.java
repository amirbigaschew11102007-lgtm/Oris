package com.example.service;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    String saveImage(Part part) throws IOException;
    String originalFileName(Part part);
    Path resolve(String filename) throws IOException;
    void deleteQuietly(String filename);
}
