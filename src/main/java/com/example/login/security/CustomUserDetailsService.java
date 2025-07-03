package com.example.login.security;

import com.example.login.mapper.db1.UserMapperDb1;
import com.example.login.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserMapperDb1 userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("=== CustomUserDetailsService.loadUserByUsername() START - username: {} ===", username);
        try {
            User user = userMapper.findByUsername(username);
            if (user == null) {
                logger.warn("=== CustomUserDetailsService.loadUserByUsername() USER NOT FOUND ===");
                throw new UsernameNotFoundException("User not found");
            }

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            
            logger.info("=== CustomUserDetailsService.loadUserByUsername() END - User loaded successfully ===");
            return userDetails;
        } catch (Exception e) {
            logger.error("=== CustomUserDetailsService.loadUserByUsername() ERROR ===", e);
            throw e;
        }
    }
}
