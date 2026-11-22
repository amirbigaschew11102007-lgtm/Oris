package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadedFile {
    private final String originalName;
    private final String contentType;
    private final byte[] content;

    public long getSize() {
        return content == null ? 0 : content.length;
    }
}
