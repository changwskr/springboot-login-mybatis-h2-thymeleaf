package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String loginForm() {
        logger.info("1) GET /login - login form page loaded");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        logger.info("2) POST /login - Login attempt for username={}", username);

        User user = loginService.login(username, password);

        if (user != null) {
            logger.info("Login successful for username={}", username);
            model.addAttribute("user", user);
            return "welcome";
        } else {
            logger.warn("Login failed for username={}", username);
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}
