package com.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User,String> {

    //Here we write extra method for DB related Operation
    // we can write custom query method 
    // and we can also write cutome finder method 

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email,String password);
}
