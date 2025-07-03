package com.example.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
    //==================================================================
    @Autowired
    ApplicationContext context;
    //==================================================================


    @GetMapping("/welcome")
    public String welcome() {
        logger.info("=== WelcomeController.welcome() START ===");
        try {
            logger.info("=== WelcomeController.welcome() END ===");
            printAllBeans();
            return "welcome";
        } catch (Exception e) {
            logger.error("=== WelcomeController.welcome() ERROR ===", e);
            throw e;
        }
    }

    //==================================================================
    public void printAllBeans() {
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            System.out.println("Bean: " + name);
        }
    }
    //==================================================================
}
