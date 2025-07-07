package com.example.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan(basePackages = "com.example.login.deposite.business.dc.dao.mapper")
public class LoginApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginApplication.class);
    
    public static void main(String[] args) {
        logger.info("=== Spring Boot Login Application Starting ===");
        logger.info("Application startup initiated");
        
        try {
            SpringApplication.run(LoginApplication.class, args);
            logger.info("=== Spring Boot Login Application Started Successfully ===");
        } catch (Exception e) {
            logger.error("=== Spring Boot Login Application Failed to Start ===", e);
            throw e;
        }
    }
}
