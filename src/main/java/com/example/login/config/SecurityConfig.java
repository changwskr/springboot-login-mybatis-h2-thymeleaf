package com.example.login.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("=== SecurityConfig.securityFilterChain() START ===");
        try {
            http
                .authorizeHttpRequests(auth -> auth
                    .antMatchers("/login", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/welcome", true)
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                )
                .csrf(csrf -> csrf.disable()) // H2 콘솔과 REST 호출을 위해 비활성화 (실무에선 재설정 필요)
                .headers(headers -> headers.frameOptions().disable()); // H2 콘솔 iframe 허용

            logger.info("=== SecurityConfig.securityFilterChain() END ===");
            return http.build();
        } catch (Exception e) {
            logger.error("=== SecurityConfig.securityFilterChain() ERROR ===", e);
            throw e;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("=== SecurityConfig.passwordEncoder() START ===");
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            logger.info("=== SecurityConfig.passwordEncoder() END ===");
            return encoder;
        } catch (Exception e) {
            logger.error("=== SecurityConfig.passwordEncoder() ERROR ===", e);
            throw e;
        }
    }
}
