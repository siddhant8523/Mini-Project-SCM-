package com.scm.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.scm.entities.User;
import com.scm.halpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger=LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserService userService;

    
    @ModelAttribute
    public void addLoggedInUserInformation(Model model,Authentication authentication){
        System.out.println("This is addLoggedInUserInformation method running from RootController....");
        if (authentication == null) {
            return;  
        }
        String username=Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}",username);
        User user=userService.getUserByEmail(username);
        System.out.println("This is RootController Log.........");
        // System.out.println(user.getName());
        // System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);
    // logger.info("User logged in: {}", username);
    }

}
