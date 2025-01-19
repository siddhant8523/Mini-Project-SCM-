package com.scm.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="user")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    // This field used to store user Information 
    @Id
    private String userId;
    @Column(name = "user_name",nullable=false)
    private String name;
    @Column(unique = true,nullable=false)
    private String email;
    private String password;
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String profilePic;
    private String phoneNumber;

    //This Field  used to Verify user information 
    private boolean enabled=false; 
    private boolean emailVerified=false;
    private boolean phoneVerified=false;

    //This field uses Enum Where Enum have providers Which is for verification
    // SELF,GOOGLE,GITHUB,LINKEDIN
    private Providers providers=Providers.SELF;
    private String providerUserId;

    //User has Multiple contact adding OneToMany Relation fields
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Contact> contacts=new ArrayList<>();

}
