package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
public class User implements UserDetails{
    // This field used to store user Information 
    @Id
    private String userId;
    @Column(name = "user_name",nullable=false)
    private String name;
    @Column(unique = true,nullable=false)
    private String email;
    @Getter(AccessLevel.NONE)
    private String password;
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String profilePic;
    private String phoneNumber;

    //This Field  used to Verify user information 
    @Getter(AccessLevel.NONE)
    private boolean enabled=true; 
    private boolean emailVerified=false;
    private boolean phoneVerified=false;

    //This field uses Enum Where Enum have providers Which is for verification
    // SELF,GOOGLE,GITHUB,LINKEDIN
    @Enumerated(value = EnumType.STRING)
    private Providers providers=Providers.SELF;
    private String providerUserId;

    //User has Multiple contact adding OneToMany Relation fields
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Contact> contacts=new ArrayList<>();

    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles=roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList()); 
        return roles;
    }

    //we consider email name as username 
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword(){
        return this.password;
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
    return true;
    }
    @Override
    public boolean isEnabled() {
    return enabled;
    }
}
