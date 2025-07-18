package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users/db1")
public class UserDb1Controller {
    @Autowired
    private UserService userService;

    private List<String> jobOptions = Arrays.asList("Developer", "Manager", "Designer", "Tester");
    private List<String> companyOptions = Arrays.asList("OpenAI", "Google", "Amazon", "Microsoft");

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("jobs", jobOptions);
        model.addAttribute("companies", companyOptions);
        return "users/create";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            return "users/create";
        }
        userService.save(user);
        return "redirect:/users/db1";
    }

    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("jobs", jobOptions);
        model.addAttribute("companies", companyOptions);
        return "users/edit";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            return "users/edit";
        }
        userService.update(user);
        return "redirect:/users/db1";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.delete(username);
        return "redirect:/users/db1";
    }
} 