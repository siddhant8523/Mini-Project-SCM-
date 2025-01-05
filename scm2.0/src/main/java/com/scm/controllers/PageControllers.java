package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageControllers {

    @RequestMapping("/home")
    public String home(){
    String str="Home Page handler";
    return str;
    }

    
    @RequestMapping("/about")
    public String aboutPage(){
    String str="About Page handler";
    return str;
    }
    @RequestMapping("/services")
    public String servicesPage(){
    String str="Services Page handler";
    return str;
    }
}
