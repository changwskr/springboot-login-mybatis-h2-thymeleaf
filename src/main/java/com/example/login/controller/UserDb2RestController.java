package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserDb2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users/db2")
public class UserDb2RestController {

    private static final Logger logger = LoggerFactory.getLogger(UserDb2RestController.class);

    @Autowired
    private UserDb2Service userService;

    /**
     * DB2의 모든 사용자 조회
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("=== UserDb2RestController.getAllUsers() START ===");
        try {
            List<User> users = userService.findAll();
            logger.info("=== UserDb2RestController.getAllUsers() END - Found {} users ===", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("=== UserDb2RestController.getAllUsers() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DB2에서 특정 사용자 조회
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        logger.info("=== UserDb2RestController.getUser() START - username: {} ===", username);
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                logger.info("=== UserDb2RestController.getUser() END - User found ===");
                return ResponseEntity.ok(user);
            } else {
                logger.warn("=== UserDb2RestController.getUser() END - User not found ===");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("=== UserDb2RestController.getUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DB2에 새 사용자 생성
     */
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        logger.info("=== UserDb2RestController.createUser() START - username: {} ===", user.getUsername());
        try {
            userService.save(user);
            logger.info("=== UserDb2RestController.createUser() END - User created successfully ===");
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            logger.error("=== UserDb2RestController.createUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    /**
     * DB2에서 사용자 정보 수정
     */
    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @Valid @RequestBody User user) {
        logger.info("=== UserDb2RestController.updateUser() START - username: {} ===", username);
        try {
            if (!username.equals(user.getUsername())) {
                return ResponseEntity.badRequest().body("Username mismatch");
            }
            userService.update(user);
            logger.info("=== UserDb2RestController.updateUser() END - User updated successfully ===");
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            logger.error("=== UserDb2RestController.updateUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

    /**
     * DB2에서 사용자 삭제
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        logger.info("=== UserDb2RestController.deleteUser() START - username: {} ===", username);
        try {
            userService.delete(username);
            logger.info("=== UserDb2RestController.deleteUser() END - User deleted successfully ===");
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            logger.error("=== UserDb2RestController.deleteUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }
} 