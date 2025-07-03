package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        logger.info("=== UserRestController.getAllUsers() START ===");
        try {
            List<User> users = userService.findAll();
            logger.info("=== UserRestController.getAllUsers() END - Found {} users ===", users.size());
            return users;
        } catch (Exception e) {
            logger.error("=== UserRestController.getAllUsers() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        logger.info("=== UserRestController.getUser() START - username: {} ===", username);
        try {
            User user = userService.findByUsername(username);
            logger.info("=== UserRestController.getUser() END - User found: {} ===", user != null);
            return user;
        } catch (Exception e) {
            logger.error("=== UserRestController.getUser() ERROR ===", e);
            throw e;
        }
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        logger.info("=== UserRestController.createUser() START - username: {} ===", user.getUsername());
        try {
            userService.save(user);
            logger.info("=== UserRestController.createUser() END - User created successfully ===");
        } catch (Exception e) {
            logger.error("=== UserRestController.createUser() ERROR ===", e);
            throw e;
        }
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        logger.info("=== UserRestController.updateUser() START - username: {} ===", user.getUsername());
        try {
            userService.update(user);
            logger.info("=== UserRestController.updateUser() END - User updated successfully ===");
        } catch (Exception e) {
            logger.error("=== UserRestController.updateUser() ERROR ===", e);
            throw e;
        }
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        logger.info("=== UserRestController.deleteUser() START - username: {} ===", username);
        try {
            userService.delete(username);
            logger.info("=== UserRestController.deleteUser() END - User deleted successfully ===");
        } catch (Exception e) {
            logger.error("=== UserRestController.deleteUser() ERROR ===", e);
            throw e;
        }
    }
}
