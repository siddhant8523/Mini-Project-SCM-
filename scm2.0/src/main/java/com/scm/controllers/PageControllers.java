package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageControllers {

    @RequestMapping("/home")
    public String home(){
    System.out.println("Home Page handler");
    return "home";
    }

    
    @RequestMapping("/about")
    public String aboutPage(){
    System.out.println("About Page handler");
    return "about";
    }
    @RequestMapping("/services")
    public String servicesPage(){
    System.out.println("Services Page handler");
    return "services";
    }
    @GetMapping("/login")
    public String login(){
        return new String("login");
    }
    @GetMapping("/contact")
    public String contact(){
        return new String("contact");
    }
    @GetMapping("/register")
    public String register(){
        return new String("register");
    }


}
