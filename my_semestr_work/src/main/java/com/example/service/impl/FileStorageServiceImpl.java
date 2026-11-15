package com.example.service.impl;

import com.example.service.FileStorageService;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class FileStorageServiceImpl implements FileStorageService {
    private static final Long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private final Path path;

    public FileStorageServiceImpl(String uploadDirectory) {
        this.path = Path.of(uploadDirectory).toAbsolutePath().normalize();

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось создать папку загрузок: " + path, e);
        }
    }


    @Override
    // Part Это объект из Servlet API, который представляет отдельную часть отправленного запроса.
    public String saveImage(Part part) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        if (part.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Максимальный размер изображения — 5 МБ.");
        }
        String contentType = part.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("Не удалось определить формат изображения.");
        }
        contentType = contentType.toLowerCase(Locale.ROOT);
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Разрешены только JPG, PNG, GIF и WEBP.");
        }
        String fileName = UUID.randomUUID() + getExtension(contentType);
        Path destination = path.resolve(fileName).normalize();
        //доп проверки на префикс(одинаковое начало)
        if (!destination.startsWith(path)) {
            throw new IOException("Некорректный путь файла.");
        }

        try (InputStream inputStream = part.getInputStream()) {
            Files.copy(inputStream, destination);
        }
        //проверяем сигнатуру файла, чтобы чел не добавил файл image.txt просто изменив на image.jpg
        if (!hasValidSignature(destination, contentType)) {
            Files.delete(destination);
            throw new IllegalArgumentException("Содержимое файла не похоже на выбранный формат изображения.");
        }
        return fileName;
    }

    @Override
    public String originalFileName(Part part) {
        if (part == null || part.getSubmittedFileName() == null) {
            return "image";
        }
        // Убираю возможный путь от браузера и сохраняю только имя файла.
        String submitted = part.getSubmittedFileName().replace("\\", "/");
        String name = submitted.substring(submitted.lastIndexOf('/') + 1);
        if (name.length() > 255) {
            name = name.substring(name.length() - 255);
        }
        return name;
    }

    @Override
    public Path resolve(String filename) throws IOException {
        if(filename==null || filename.isBlank() || filename.contains("/") || filename.contains("\\")){
            throw new IOException("Некорректное имя файла.");
        }
        Path file = path.resolve(filename).normalize();
        if(!file.startsWith(path)){
            throw new IOException("Недопустимый путь.");
        }
        return file;
    }

    @Override
    public void deleteQuietly(String filename) {
        if(filename==null){
            return;
        }
        try{
            Files.deleteIfExists(resolve(filename));
        } catch (IOException e) {
            // Ошибка удаления временного файла не должна скрывать основную ошибку.
        }
    }

    private String getExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Неподдерживаемый формат изображения.");
        };
    }

    private boolean hasValidSignature(Path file, String contentType) throws IOException {
        byte[] bytes = new byte[12];
        int count;

        try (InputStream input = Files.newInputStream(file)) {
            count = input.readNBytes(bytes, 0, bytes.length);
        }

        if (count < 6) {
            return false;
        }

        return switch (contentType) {
            case "image/jpeg" -> count >= 3
                    && (bytes[0] & 0xFF) == 0xFF
                    && (bytes[1] & 0xFF) == 0xD8
                    && (bytes[2] & 0xFF) == 0xFF;
            case "image/png" -> count >= 8
                    && (bytes[0] & 0xFF) == 0x89
                    && bytes[1] == 0x50
                    && bytes[2] == 0x4E
                    && bytes[3] == 0x47
                    && bytes[4] == 0x0D
                    && bytes[5] == 0x0A
                    && bytes[6] == 0x1A
                    && bytes[7] == 0x0A;
            case "image/gif" -> bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F';
            case "image/webp" -> count >= 12
                    && bytes[0] == 'R' && bytes[1] == 'I' && bytes[2] == 'F' && bytes[3] == 'F'
                    && bytes[8] == 'W' && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P';
            default -> false;
        };
    }
}
