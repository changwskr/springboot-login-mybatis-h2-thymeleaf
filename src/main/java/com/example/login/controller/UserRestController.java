package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        logger.info("=== UserRestController.getAllUsers() START ===");
        try {
            List<User> users = userService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Users retrieved successfully");
            response.put("data", users);
            response.put("count", users.size());
            logger.info("=== UserRestController.getAllUsers() END - Found {} users ===", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== UserRestController.getAllUsers() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve users");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String username) {
        logger.info("=== UserRestController.getUser() START - username: {} ===", username);
        try {
            User user = userService.findByUsername(username);
            Map<String, Object> response = new HashMap<>();
            if (user != null) {
                response.put("success", true);
                response.put("message", "User found");
                response.put("data", user);
                logger.info("=== UserRestController.getUser() END - User found ===");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "User not found");
                logger.warn("=== UserRestController.getUser() END - User not found ===");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("=== UserRestController.getUser() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody User user) {
        logger.info("=== UserRestController.createUser() START - username: {} ===", user.getUsername());
        try {
            userService.save(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User created successfully");
            response.put("data", user);
            logger.info("=== UserRestController.createUser() END - User created successfully ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("=== UserRestController.createUser() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String username, @Valid @RequestBody User user) {
        logger.info("=== UserRestController.updateUser() START - username: {} ===", username);
        try {
            if (!username.equals(user.getUsername())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Username mismatch");
                return ResponseEntity.badRequest().body(response);
            }
            userService.update(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("data", user);
            logger.info("=== UserRestController.updateUser() END - User updated successfully ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== UserRestController.updateUser() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String username) {
        logger.info("=== UserRestController.deleteUser() START - username: {} ===", username);
        try {
            userService.delete(username);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deleted successfully");
            response.put("username", username);
            logger.info("=== UserRestController.deleteUser() END - User deleted successfully ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== UserRestController.deleteUser() ERROR ===", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
