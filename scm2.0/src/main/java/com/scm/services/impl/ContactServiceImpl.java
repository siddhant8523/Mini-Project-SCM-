package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.halpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo repo;

    @Override
    public void delete(String id) {
        Contact contact=repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact Not Found with given id"+id));
            repo.delete(contact);

        
    }

    @Override
    public List<Contact> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Contact getById(String id) {
       
        return repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact Not Found with given id"+id));
       }

    @Override
    public Contact save(Contact contact) {
        String contactId=UUID.randomUUID().toString();
        contact.setId(contactId);

        return repo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
            var contactOld=repo.findById(contact.getId()).orElseThrow(()->new ResourceNotFoundException("Contact Not Found"));
            contactOld.setName(contact.getName());
            contactOld.setEmail(contact.getEmail());
            contactOld.setPhoneNumber(contact.getPhoneNumber());
            contactOld.setAddress(contact.getAddress());
            contactOld.setDescription(contact.getDescription());
            contactOld.setPicture(contact.getPicture());
            contactOld.setFavorite(contact.getFavorite());
            contactOld.setWebsiteLink(contact.getWebsiteLink());
            contactOld.setLinkedInLink(contact.getLinkedInLink());
            contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());



        return repo.save(contactOld);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword,int size,int page,String sortBy,String order,User user) {
    
        Sort sort=order.equals("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        //page=Page no. number , size=no. of content on that page 
        var pagable=PageRequest.of(page,size,sort);
        return repo.findByUserAndNameContaining(user,nameKeyword, pagable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword,int size,int page,String sortBy,String order,User user) {
        Sort sort=order.equals("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        //page=Page no. number , size=no. of content on that page 
        var pagable=PageRequest.of(page,size,sort);
        return repo.findByUserAndEmailContaining( user,emailKeyword,pagable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword,int size,int page,String sortBy,String order,User user) {
        Sort sort=order.equals("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        //page=Page no. number , size=no. of content on that page 
        var pagable=PageRequest.of(page,size,sort);
        return repo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword,pagable);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        
        return repo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String sortField,String sortDirection) {
        Sort sort=sortDirection.equals("desc")? Sort.by(sortField).descending():Sort.by(sortField).ascending();
        //page=Page no. number , size=no. of content on that page 
        var pagable=PageRequest.of(page,size,sort);
        var pageContacts = repo.findByUser(user, pagable);
        return pageContacts;        
        
    }

}
