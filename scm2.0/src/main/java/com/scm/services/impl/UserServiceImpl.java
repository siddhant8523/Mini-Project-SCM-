package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.halpers.AppConstants;
import com.scm.halpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    //injecting repo 
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    //using Logger For Exception Handling
    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Override
    public void deleteUser(String id) {
        User user=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not Found :( "));
        userRepo.delete(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user=userRepo.findById(userId).orElse(null);
        return user!=null ? true :false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user=userRepo.findByEmail(email).orElse(null);
        return user!=null ? true :false;
    }

    @Override
    public User saveUser(User user) {
        //generate user Id
        String userId=UUID.randomUUID().toString();
        user.setUserId(userId);
        //password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info(user.getProviders().toString());
        return userRepo.save(user);
    }

    @Override
    public Optional<User> updateUser(User user) {

        User user2=userRepo.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("User Not Found :( "));
        //Here We are getting the data of user from controller by using DTO and setting it in user2 
        //which is an entity which will further save in database cuz Entity has direct Relation with DB.
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setAbout(user.getAbout());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProviders(user.getProviders());
        user2.setProviderUserId(user.getProviderUserId());

        User save=userRepo.save(user2);
        return Optional.ofNullable(save);
    }

} 
