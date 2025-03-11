package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.halpers.AppConstants;
import com.scm.halpers.Helper;
import com.scm.halpers.Message;
import com.scm.halpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger=LoggerFactory.getLogger(ContactController.class);
    @Autowired
    private ImageService imageService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public String addContactView(Model model){
            ContactForm contactForm=new ContactForm();

            model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String saveContact( @Valid @ModelAttribute ContactForm contactForm,BindingResult result,Authentication authentication,HttpSession httpSession){

        //validation form field 
        if(result.hasErrors()){
            result.getAllErrors().forEach(error->logger.info(error.toString()));
            httpSession.setAttribute("message",Message.builder()
            .content("Please fill the Valid Information !")
            .type(MessageType.red)
            .build());
            return "user/add_contact";
        }

        String username=Helper.getEmailOfLoggedInUser(authentication);
        User user=userService.getUserByEmail(username);
        

        //saving data from form to contact
        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setFavorite(contactForm.isFavorite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setUser(user);
        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
            //creating Randome file name 
            String filename=UUID.randomUUID().toString();
            //Processing the imgae here 
            //uploading the image 
            String fileURL=imageService.uploadImage(contactForm.getContactImage(),filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename);
        }
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contactService.save(contact);
        System.out.println(contactForm);


        httpSession.setAttribute("message",Message.builder()
        .content("You have successfully added a new contact !")
        .type(MessageType.green)
        .build());
        return "redirect:/user/contacts/add";
    }

    @RequestMapping()
    public String viewContact(
            @RequestParam(value="page",defaultValue="0") int page,
            @RequestParam(value="size",defaultValue=AppConstants.PAGE_SIZE+"")int size,
            @RequestParam(value="sortBy",defaultValue="name")String sortBy,
            @RequestParam(value="direction",defaultValue="asc")String direction,
            Model model,Authentication authentication){

        String username=Helper.getEmailOfLoggedInUser(authentication);
        User user=userService.getUserByEmail(username);
        Page<Contact> contact=contactService.getByUser(user,page,size,sortBy,direction);
        model.addAttribute("contact", contact);     
        
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
                @ModelAttribute ContactSearchForm contactSearchForm,
                @RequestParam(value="size",defaultValue=AppConstants.PAGE_SIZE+"")int size,
                @RequestParam(value="page",defaultValue="0") int page,
                @RequestParam(value="sortBy",defaultValue = "name") String sortBy,
                @RequestParam(value="direction",defaultValue = "asc") String direction,
                Model model,
                Authentication authentication
    ){
        logger.info("field {} keyword{}",contactSearchForm.getField(),contactSearchForm.getValue());
        var user=userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContact=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact=contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,user);

        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact=contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContact=contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }
            
        model.addAttribute("contactSearchForm", contactSearchForm);
        logger.info("pageContact {}",pageContact);
        model.addAttribute("contact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/search";
    }

    @RequestMapping("/delete/{id}")
    public String deleteContacts(@PathVariable("id") String id){
        contactService.delete(id);
        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{contactId}")
    public String updateContactForm(@PathVariable("contactId") String contactId,Model model){
        var contact=contactService.getById(contactId);
        ContactForm contactForm=new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.getFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactForm",contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact";
    }


    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable("contactId") String contactId,@Valid @ModelAttribute ContactForm contactForm,BindingResult result,Model model){


        //Validating form updation
        if(result.hasErrors()){
            return "user/update_contact";
        }

        var contactUpdate=contactService.getById(contactId);
        contactUpdate.setId(contactId);
        contactUpdate.setName(contactForm.getName());
        contactUpdate.setEmail(contactForm.getEmail());
        contactUpdate.setPhoneNumber(contactForm.getPhoneNumber());
        contactUpdate.setAddress(contactForm.getAddress());
        contactUpdate.setDescription(contactForm.getDescription());
        contactUpdate.setFavorite(contactForm.isFavorite());
        contactUpdate.setWebsiteLink(contactForm.getWebsiteLink());
        contactUpdate.setLinkedInLink(contactForm.getLinkedInLink());

        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
            String fileName=UUID.randomUUID().toString();
            String imageURL=imageService.uploadImage(contactForm.getContactImage(), fileName);
            contactUpdate.setCloudinaryImagePublicId(fileName);
            contactForm.setPicture(imageURL);
            contactUpdate.setPicture(imageURL);

        }
        var updateContact=contactService.update(contactUpdate);
        logger.info("updated contact {}",updateContact);
        model.addAttribute("message",Message.builder().content("Contact Updated").type(MessageType.green));
        return "redirect:/user/contacts/view/" + contactId;

    }
}
