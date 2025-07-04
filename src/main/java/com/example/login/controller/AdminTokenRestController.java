package com.example.login.controller;

import com.example.login.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/token")
public class AdminTokenRestController {

    private static final Logger logger = LoggerFactory.getLogger(AdminTokenRestController.class);

    @Autowired
    private TokenService tokenService;

    /**
     * 토큰 생성
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateToken() {
        logger.info("=== AdminTokenRestController.generateToken() START ===");
        try {
            String token = tokenService.generateToken();
            String filePath = tokenService.saveTokenToFile(token);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Token generated successfully");
            response.put("token", token);
            response.put("filePath", filePath);
            response.put("timestamp", System.currentTimeMillis());
            
            logger.info("=== AdminTokenRestController.generateToken() END - Token generated successfully ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== AdminTokenRestController.generateToken() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to generate token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 토큰 생성 상태 확인
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getTokenStatus() {
        logger.info("=== AdminTokenRestController.getTokenStatus() START ===");
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Token service is available");
            response.put("timestamp", System.currentTimeMillis());
            logger.info("=== AdminTokenRestController.getTokenStatus() END ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== AdminTokenRestController.getTokenStatus() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Token service is not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 