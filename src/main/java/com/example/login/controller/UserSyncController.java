package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.UserSyncService;
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
@RequestMapping("/users/sync")
public class UserSyncController {

    private static final Logger logger = LoggerFactory.getLogger(UserSyncController.class);

    @Autowired
    private UserSyncService userSyncService;

    private List<String> jobOptions = Arrays.asList("Developer", "Manager", "Designer", "Tester");
    private List<String> companyOptions = Arrays.asList("OpenAI", "Google", "Amazon", "Microsoft");

    /**
     * 두 DB의 모든 사용자 목록 조회
     */
    @GetMapping
    public String listAllUsers(Model model) {
        logger.info("=== UserSyncController.listAllUsers() START ===");
        try {
            List<User> users = userSyncService.findAllFromBothDatabases();
            model.addAttribute("users", users);
            logger.info("=== UserSyncController.listAllUsers() END - Found {} users ===", users.size());
            return "users/sync/list";
        } catch (Exception e) {
            logger.error("=== UserSyncController.listAllUsers() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 등록 폼 표시
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        logger.info("=== UserSyncController.showCreateForm() START ===");
        try {
            model.addAttribute("user", new User());
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserSyncController.showCreateForm() END ===");
            return "users/sync/create";
        } catch (Exception e) {
            logger.error("=== UserSyncController.showCreateForm() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자를 두 DB에 동시에 등록
     */
    @PostMapping
    public String createUserInBothDatabases(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        logger.info("=== UserSyncController.createUserInBothDatabases() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserSyncController.createUserInBothDatabases() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/sync/create";
            }
            
            userSyncService.saveToBothDatabases(user);
            logger.info("=== UserSyncController.createUserInBothDatabases() END - User created in both databases ===");
            return "redirect:/users/sync";
        } catch (Exception e) {
            logger.error("=== UserSyncController.createUserInBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 수정 폼 표시
     */
    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        logger.info("=== UserSyncController.showEditForm() START - username: {} ===", username);
        try {
            User user = userSyncService.findByUsernameFromBothDatabases(username);
            if (user == null) {
                logger.warn("=== UserSyncController.showEditForm() USER NOT FOUND ===");
                return "redirect:/users/sync";
            }
            
            model.addAttribute("user", user);
            model.addAttribute("jobs", jobOptions);
            model.addAttribute("companies", companyOptions);
            logger.info("=== UserSyncController.showEditForm() END ===");
            return "users/sync/edit";
        } catch (Exception e) {
            logger.error("=== UserSyncController.showEditForm() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자 정보를 두 DB에서 동시에 수정
     */
    @PostMapping("/update")
    public String updateUserInBothDatabases(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        logger.info("=== UserSyncController.updateUserInBothDatabases() START - username: {} ===", user.getUsername());
        try {
            if (result.hasErrors()) {
                logger.warn("=== UserSyncController.updateUserInBothDatabases() VALIDATION ERRORS ===");
                model.addAttribute("jobs", jobOptions);
                model.addAttribute("companies", companyOptions);
                return "users/sync/edit";
            }
            
            userSyncService.updateInBothDatabases(user);
            logger.info("=== UserSyncController.updateUserInBothDatabases() END - User updated in both databases ===");
            return "redirect:/users/sync";
        } catch (Exception e) {
            logger.error("=== UserSyncController.updateUserInBothDatabases() ERROR ===", e);
            throw e;
        }
    }

    /**
     * 사용자를 두 DB에서 동시에 삭제
     */
    @GetMapping("/delete/{username}")
    public String deleteUserFromBothDatabases(@PathVariable String username) {
        logger.info("=== UserSyncController.deleteUserFromBothDatabases() START - username: {} ===", username);
        try {
            userSyncService.deleteFromBothDatabases(username);
            logger.info("=== UserSyncController.deleteUserFromBothDatabases() END - User deleted from both databases ===");
            return "redirect:/users/sync";
        } catch (Exception e) {
            logger.error("=== UserSyncController.deleteUserFromBothDatabases() ERROR ===", e);
            throw e;
        }
    }
} 