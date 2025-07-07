package com.example.login.service;

import com.example.login.mapper.db1.UserMapperDb1;
import com.example.login.mapper.db2.UserMapperDb2;
import com.example.login.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapperDb1 userMapperDb1;
    @Autowired
    private UserMapperDb2 userMapperDb2;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        logger.info("=== UserService.findAll() START ===");
        try {
            List<User> users = userMapperDb1.findAll();
            for (User user : users) {
                user.setDbName("DB1");
            }
            logger.info("=== UserService.findAll() END - Found {} users from DB1 ===", users.size());
            return users;
        } catch (Exception e) {
            logger.error("=== UserService.findAll() ERROR ===", e);
            throw e;
        }
    }

    public User findByUsername(String username) {
        logger.info("=== UserService.findByUsername() START - username: {} ===", username);
        try {
            User user = userMapperDb1.findByUsername(username);
            if (user != null) {
                user.setDbName("DB1");
            }
            logger.info("=== UserService.findByUsername() END - User found: {} ===", user != null);
            return user;
        } catch (Exception e) {
            logger.error("=== UserService.findByUsername() ERROR ===", e);
            throw e;
        }
    }

    public void save(User user) {
        logger.info("=== UserService.save() START - username: {} ===", user.getUsername());
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if ("changwskr".equals(user.getUsername())) {
                user.setDbName("DB2");
                userMapperDb2.insert(user);
            } else {
                user.setDbName("DB1");
                userMapperDb1.insert(user);
            }
            logger.info("=== UserService.save() END - User saved successfully to {} ===", user.getDbName());
        } catch (Exception e) {
            logger.error("=== UserService.save() ERROR ===", e);
            throw e;
        }
    }

    public void update(User user) {
        logger.info("=== UserService.update() START - username: {} ===", user.getUsername());
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if ("changwskr".equals(user.getUsername())) {
                user.setDbName("DB2");
                userMapperDb2.update(user);
            } else {
                user.setDbName("DB1");
                userMapperDb1.update(user);
            }
            logger.info("=== UserService.update() END - User updated successfully in {} ===", user.getDbName());
        } catch (Exception e) {
            logger.error("=== UserService.update() ERROR ===", e);
            throw e;
        }
    }

    public void delete(String username) {
        logger.info("=== UserService.delete() START - username: {} ===", username);
        try {
            userMapperDb1.delete(username);
            logger.info("=== UserService.delete() END - User deleted successfully from DB1 ===");
        } catch (Exception e) {
            logger.error("=== UserService.delete() ERROR ===", e);
            throw e;
        }
    }
}
