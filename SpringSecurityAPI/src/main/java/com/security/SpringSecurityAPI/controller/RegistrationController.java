package com.security.SpringSecurityAPI.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.security.SpringSecurityAPI.model.User;
import com.security.SpringSecurityAPI.service.EmailService;
import com.security.SpringSecurityAPI.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {

        logger.info("Register page requested with data {}", user);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView processRegistrationForm(
            ModelAndView modelAndView,
            @Valid User user,
            BindingResult bindingResult,
            HttpServletRequest request) {

        logger.info("Register page requested with data {}", user);

        User userExists = userService.findByEmail(user.getEmail());

        logger.info("User exists {}", userExists);

        System.out.println("userExists: " + userExists);
        
        // ensures every user has a unique email address
        if (userExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            modelAndView.setViewName("register");
            
            logger.info("User already exists with email {}", user.getEmail());

            bindingResult.reject("email");

            logger.warn("User rejected with binding result {}", bindingResult);
        }

        // ensures that the user has entered a valid fields
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");

            logger.warn("binding result has some errors {}", bindingResult);
        } else {
            user.setEnabled(false); // because the user has not setup the password yet
            user.setConfirmationToken(UUID.randomUUID().toString());    // generates a random token
            // UUID is utility class that allows the generation of tokens.
            // UUID is a 128-bit number that is used to identify information in a distributed system.

            userService.saveUser(user); // saves the user to the database

            /** 
             * getScheme() returns the scheme weather request is http or https.
             * getServerName() returns the server name
            */
            String appUrl = request.getScheme() + "://" + request.getServerName() + ":8080";

            // sends an email to the user with a link to /confirm page
            String message = "To set your password, please click the link below:\n"
                + appUrl + "/confirm?token=" + user.getConfirmationToken();

            emailService.sendEmail(user.getEmail(), "Please set a password", message);

            modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail() + ".");
            
            modelAndView.setViewName("register");   // will sends the user to register page
        }

        return modelAndView;
    }

    // cofirm the user registration
    @GetMapping("/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {
        logger.info("Confirm registration requested with token {}", token);
        
        User user = userService.findByConfirmationToken(token);
        logger.info("User found {}", user);

        if (user == null) {
            modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
            logger.warn("User not found with token {}", token);
        } else {
            modelAndView.addObject("confirmationToken", user.getConfirmationToken());
            logger.info("User found with token {}", token);
        }

        modelAndView.setViewName("confirm");

        logger.info("success");
        return modelAndView;
    }

    // confirmRegistration
    @PostMapping("/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam  Map<String, String> requestParams, RedirectAttributes redirect) {

        logger.info("confirmRegistration requested with token {}", requestParams.get("token"));

        User user = userService.findByConfirmationToken(requestParams.get("token"));

        logger.info("user found {}", user);

        user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
        logger.info("user password {}", requestParams.get("password"));
        user.setEnabled(true);
        logger.info("user enabled {}", user.isEnabled());

        userService.saveUser(user);
        logger.info("user saved {}", user);

        modelAndView.setViewName("confirm");
        modelAndView.addObject("successMessage", "Password set successfully!");

        logger.info("confirmRegistration completed");
        return modelAndView;
    }
}