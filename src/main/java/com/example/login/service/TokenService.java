package com.example.login.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class TokenService {
    private static final String TOKEN_DIR = "C:/tokens/";

    public String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String saveTokenToFile(String token) throws Exception {
        Files.createDirectories(Paths.get(TOKEN_DIR));
        String filePath = TOKEN_DIR + "token_" + System.currentTimeMillis() + ".txt";
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            writer.write(token);
        }
        return filePath;
    }
} 