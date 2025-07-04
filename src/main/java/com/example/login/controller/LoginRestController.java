package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginRestController {

    private static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 사용자 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        logger.info("=== LoginRestController.login() START - username: {} ===", loginRequest.get("username"));
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            if (username == null || password == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Username and password are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            User user = loginService.login(username, password);
            
            Map<String, Object> response = new HashMap<>();
            if (user != null) {
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", user);
                logger.info("=== LoginRestController.login() END - Login successful ===");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid username or password");
                logger.warn("=== LoginRestController.login() END - Login failed ===");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            logger.error("=== LoginRestController.login() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed due to server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        logger.info("=== LoginRestController.logout() START ===");
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout successful");
            logger.info("=== LoginRestController.logout() END - Logout successful ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== LoginRestController.logout() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Logout failed due to server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 현재 로그인 상태 확인
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLoginStatus() {
        logger.info("=== LoginRestController.getLoginStatus() START ===");
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login status check successful");
            response.put("timestamp", System.currentTimeMillis());
            logger.info("=== LoginRestController.getLoginStatus() END ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== LoginRestController.getLoginStatus() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Status check failed due to server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 