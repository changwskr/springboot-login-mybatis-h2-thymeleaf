package com.example.login.service;

import com.example.login.mapper.db1.UserMapperDb1;
import com.example.login.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserMapperDb1 userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(String username, String rawPassword) {
        logger.info("LoginService - login() started. username={}", username);

        try {
            User user = userMapper.findByUsername(username);
            if (user != null) {
                logger.info("User found in DB. Checking password match...");
                if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                    logger.info("Password match successful for username={}", username);
                    return user;
                } else {
                    logger.warn("Password mismatch for username={}", username);
                }
            } else {
                logger.warn("User not found in DB for username={}", username);
            }
        } catch (Exception e) {
            logger.error("Exception occurred during login process", e);
        }

        logger.info("LoginService - login() ended.");
        return null;
    }
}
