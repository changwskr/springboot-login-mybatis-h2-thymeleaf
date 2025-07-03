package com.example.login.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
    
    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("=== SwaggerConfig.customOpenAPI() START ===");
        try {
            OpenAPI openAPI = new OpenAPI()
                    .info(new Info().title("User API").version("1.0").description("Spring Boot User Management API"));
            logger.info("=== SwaggerConfig.customOpenAPI() END ===");
            return openAPI;
        } catch (Exception e) {
            logger.error("=== SwaggerConfig.customOpenAPI() ERROR ===", e);
            throw e;
        }
    }
}
