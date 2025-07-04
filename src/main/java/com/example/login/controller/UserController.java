package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private List<String> jobOptions = Arrays.asList("Developer", "Manager", "Designer", "Tester");
    private List<String> companyOptions = Arrays.asList("OpenAI", "Google", "Amazon", "Microsoft");

    @GetMapping
    public String listUsers(Model model) {
        logger.info("=== UserController.listUsers() START ===" + model.toString());
        try {
            List<User> users = userService.findAll();
            model.addAttribute("users", users);
            logger.info("=== UserController.listUsers() END - Found {} users ===", users.size());
            return "users/list";
        } catch (Exception e) {
            logger.error("=== UserController.listUsers() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("=== UserController.showCreateForm() START ===");
        try {
            model.addAttribute("user", new User());
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserController.showCreateForm() END ===");
            return "users/create";
        } catch (Exception e) {
            logger.error("=== UserController.showCreateForm() ERROR ===", e);
            throw e;
        }
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        logger.info("=== UserController.createUser() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserController.createUser() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/create";
            }
            userService.save(user);
            logger.info("=== UserController.createUser() END - User created successfully ===");
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("=== UserController.createUser() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        logger.info("=== UserController.showEditForm() START - username: {} ===", username);
        try {
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserController.showEditForm() END ===");
            return "users/edit";
        } catch (Exception e) {
            logger.error("=== UserController.showEditForm() ERROR ===", e);
            throw e;
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        logger.info("=== UserController.updateUser() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserController.updateUser() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/edit";
            }
            userService.update(user);
            logger.info("=== UserController.updateUser() END - User updated successfully ===");
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("=== UserController.updateUser() ERROR ===", e);
            throw e;
        }
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        logger.info("=== UserController.deleteUser() START - username: {} ===", username);
        try {
            userService.delete(username);
            logger.info("=== UserController.deleteUser() END - User deleted successfully ===");
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("=== UserController.deleteUser() ERROR ===", e);
            throw e;
        }
    }
}
