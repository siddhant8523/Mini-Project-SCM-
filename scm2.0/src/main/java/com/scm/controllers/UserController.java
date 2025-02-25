package com.scm.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {


    private Logger logger=LoggerFactory.getLogger(UserController.class);
    
    @RequestMapping(value="/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }

    @RequestMapping(value="/profile")
    public String userProfile(Model model,Authentication authentication) {
        
        return "user/profile";
    }
}
