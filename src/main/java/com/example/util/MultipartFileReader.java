package com.example.util;

import com.example.dto.UploadedFile;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;

public final class MultipartFileReader {
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    public MultipartFileReader(){}

    public static UploadedFile read(Part part) throws IOException{
        if(part == null || part.getSize() == 0){
            return null;
        }if(part.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException("Размер изображения не должен превышать 5 МБ.");
        }
        try(InputStream inputStream = part.getInputStream()){
            return new UploadedFile(
                    part.getSubmittedFileName(),
                    part.getContentType(),
                    inputStream.readAllBytes()
            );
        }
    }
}
