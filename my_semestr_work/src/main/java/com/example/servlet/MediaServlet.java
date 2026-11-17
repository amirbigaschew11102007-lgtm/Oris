package com.example.servlet;

import com.example.service.FileStorageService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/media/*")
public class MediaServlet extends HttpServlet {
    private FileStorageService fileStorageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        fileStorageService = (FileStorageService) config.getServletContext().getAttribute("fileStorageService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() < 2) {
            resp.sendError(404);
            return;
        }
        try {
            String filename = pathInfo.substring(1);
            Path path = fileStorageService.resolve(filename);

            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            resp.setContentType(contentType);
            resp.setHeader("X-Content-Type-Options", "nosniff");
            resp.setContentLengthLong(Files.size(path));

            try (var input = Files.newInputStream(path);
                 var output = resp.getOutputStream()) {
                input.transferTo(output);
            }
        }catch (IOException e) {
            resp.sendError(404);
        }
    }
}
