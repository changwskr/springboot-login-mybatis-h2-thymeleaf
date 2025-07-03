package com.example.login.controller;

import com.example.login.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/token")
public class AdminTokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public String showTokenForm() {
        return "admin/token";
    }

    @PostMapping
    public String generateToken(Model model) {
        try {
            String token = tokenService.generateToken();
            String filePath = tokenService.saveTokenToFile(token);
            model.addAttribute("token", token);
            model.addAttribute("filePath", filePath);
        } catch (Exception e) {
            model.addAttribute("error", "토큰 생성/저장 중 오류: " + e.getMessage());
        }
        return "admin/token";
    }
} 