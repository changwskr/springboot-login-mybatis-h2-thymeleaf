package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users/sync")
public class UserSyncRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserSyncRestController.class);

    @Autowired
    private UserSyncService userSyncService;

    /**
     * 두 DB의 모든 사용자 조회
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("=== UserSyncRestController.getAllUsers() START ===");
        try {
            List<User> users = userSyncService.findAllFromBothDatabases();
            logger.info("=== UserSyncRestController.getAllUsers() END - Found {} users ===", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("=== UserSyncRestController.getAllUsers() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 두 DB에서 특정 사용자 조회
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        logger.info("=== UserSyncRestController.getUser() START - username: {} ===", username);
        try {
            User user = userSyncService.findByUsernameFromBothDatabases(username);
            if (user != null) {
                logger.info("=== UserSyncRestController.getUser() END - User found ===");
                return ResponseEntity.ok(user);
            } else {
                logger.warn("=== UserSyncRestController.getUser() END - User not found ===");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("=== UserSyncRestController.getUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 두 DB에 동시에 새 사용자 생성
     */
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        logger.info("=== UserSyncRestController.createUser() START - username: {} ===", user.getUsername());
        try {
            userSyncService.saveToBothDatabases(user);
            logger.info("=== UserSyncRestController.createUser() END - User created in both databases ===");
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully in both databases");
        } catch (Exception e) {
            logger.error("=== UserSyncRestController.createUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user in both databases");
        }
    }

    /**
     * 두 DB에서 동시에 사용자 정보 수정
     */
    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @Valid @RequestBody User user) {
        logger.info("=== UserSyncRestController.updateUser() START - username: {} ===", username);
        try {
            if (!username.equals(user.getUsername())) {
                return ResponseEntity.badRequest().body("Username mismatch");
            }
            userSyncService.updateInBothDatabases(user);
            logger.info("=== UserSyncRestController.updateUser() END - User updated in both databases ===");
            return ResponseEntity.ok("User updated successfully in both databases");
        } catch (Exception e) {
            logger.error("=== UserSyncRestController.updateUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user in both databases");
        }
    }

    /**
     * 두 DB에서 동시에 사용자 삭제
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        logger.info("=== UserSyncRestController.deleteUser() START - username: {} ===", username);
        try {
            userSyncService.deleteFromBothDatabases(username);
            logger.info("=== UserSyncRestController.deleteUser() END - User deleted from both databases ===");
            return ResponseEntity.ok("User deleted successfully from both databases");
        } catch (Exception e) {
            logger.error("=== UserSyncRestController.deleteUser() ERROR ===", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user from both databases");
        }
    }
} 