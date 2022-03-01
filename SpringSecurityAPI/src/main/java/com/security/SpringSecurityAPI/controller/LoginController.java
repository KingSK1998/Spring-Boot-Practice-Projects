package com.security.SpringSecurityAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
    
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/")
    public String getHomePage() {
        logger.info("Home page requested");
        return "home";
    }
    
    @GetMapping("/login")
    public String getLoginPage() {
        logger.info("Login page requested");
        return "login";
    }
}
