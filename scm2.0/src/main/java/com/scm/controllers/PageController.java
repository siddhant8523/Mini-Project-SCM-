package com.scm.controllers;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.halpers.Message;
import com.scm.halpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {


    @Autowired
    private UserService userService; 

    @RequestMapping("/home")
    public String home(){
    System.out.println("Home Page handler");
    return "home";
    }

    @GetMapping("/")
    public String Index() {
        return "redirect:/home";
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
    public String register(Model model){
        UserForm userForm=new UserForm();
        //Setting Default Data if User reach to the Register Form 


        //Here model is used to get the data from register.html Form Tag<>
        model.addAttribute("userForm",userForm);
        return "register";
    }

    //Handler for form register Requesting 
    @RequestMapping(value="/do-register", method=RequestMethod.POST)
    public String processResgister(@Valid @ModelAttribute UserForm userForm ,BindingResult result,HttpSession session){
        System.out.println("Processing Registration");
        System.out.println(userForm);

        if(result.hasErrors()){
            return "register";
        }

        User user=new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://instagram.fdel1-2.fna.fbcdn.net/v/t51.2885-19/418604310_780339490601707_6368887317037292170_n.jpg?_nc_ht=instagram.fdel1-2.fna.fbcdn.net&_nc_cat=107&_nc_ohc=nbAVXytlDNYQ7kNvgGNixe5&_nc_gid=d9983fda04854119b4df7053790952c9&edm=ALGbJPMBAAAA&ccb=7-5&oh=00_AYAuqRhpSAZrKBVF9Ovj0mYgtIqE22BqtMRzUBSK4-5neg&oe=679E4E2B&_nc_sid=7d3ac5");
        //Here supposed to get profile pic directly from website
        User savedUser=userService.saveUser(user);
        System.out.println("User Saved");
        Message message=Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message",message);
        return "redirect:/register";
    }
}
