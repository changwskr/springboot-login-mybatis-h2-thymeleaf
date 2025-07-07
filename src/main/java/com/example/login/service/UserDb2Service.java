package com.example.login.service;

import com.example.login.mapper.db2.UserMapperDb2;
import com.example.login.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDb2Service {

    private static final Logger logger = LoggerFactory.getLogger(UserDb2Service.class);

    @Autowired
    private UserMapperDb2 userMapper;

    public List<User> findAll() {
        logger.info("=== UserDb2Service.findAll() START ===");
        try {
            List<User> users = userMapper.findAll();
            // DB2에서 조회한 사용자들에게 DB명 설정
            for (User user : users) {
                user.setDbName("DB2");
            }
            logger.info("=== UserDb2Service.findAll() END - Found {} users from DB2 ===", users.size());
            return users;
        } catch (Exception e) {
            logger.error("=== UserDb2Service.findAll() ERROR ===", e);
            throw e;
        }
    }

    public User findByUsername(String username) {
        logger.info("=== UserDb2Service.findByUsername() START - username: {} ===", username);
        try {
            User user = userMapper.findByUsername(username);
            if (user != null) {
                user.setDbName("DB2");
            }
            logger.info("=== UserDb2Service.findByUsername() END - User found: {} ===", user != null);
            return user;
        } catch (Exception e) {
            logger.error("=== UserDb2Service.findByUsername() ERROR ===", e);
            throw e;
        }
    }

    public void save(User user) {
        logger.info("=== UserDb2Service.save() START - username: {} ===", user.getUsername());
        try {
            user.setDbName("DB2");
            userMapper.insert(user);
            logger.info("=== UserDb2Service.save() END - User saved successfully to DB2 ===");
        } catch (Exception e) {
            logger.error("=== UserDb2Service.save() ERROR ===", e);
            throw e;
        }
    }

    public void update(User user) {
        logger.info("=== UserDb2Service.update() START - username: {} ===", user.getUsername());
        try {
            user.setDbName("DB2");
            userMapper.update(user);
            logger.info("=== UserDb2Service.update() END - User updated successfully in DB2 ===");
        } catch (Exception e) {
            logger.error("=== UserDb2Service.update() ERROR ===", e);
            throw e;
        }
    }

    public void delete(String username) {
        logger.info("=== UserDb2Service.delete() START - username: {} ===", username);
        try {
            userMapper.delete(username);
            logger.info("=== UserDb2Service.delete() END - User deleted successfully from DB2 ===");
        } catch (Exception e) {
            logger.error("=== UserDb2Service.delete() ERROR ===", e);
            throw e;
        }
    }
} 