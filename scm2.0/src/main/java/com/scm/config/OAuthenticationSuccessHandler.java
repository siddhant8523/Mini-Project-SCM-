package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.halpers.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo repo;
    Logger logger=LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);  
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

                
            var oauth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
            String authorizedClientRegistrationId=oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            logger.info(authorizedClientRegistrationId);

            logger.info("OAuthenticationSuccessHandle");

            var oauthUser=(DefaultOAuth2User)authentication.getPrincipal();
            User user=new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setRoleList(List.of(AppConstants.ROLE_USER));
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setPassword("dummyPassword ");



            //identify the proivider

            if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

                //fetching the data when login with google
                user.setEmail(oauthUser.getAttribute("email").toString());
                user.setName(oauthUser.getAttribute("name").toString());
                user.setProfilePic(oauthUser.getAttribute("picture").toString());
                user.setProviderUserId(oauthUser.getName());
                user.setProviders(Providers.GOOGLE);
                user.setAbout("This account created using Google");


            }
            else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){

                //fetching the data using github login
                String email=oauthUser.getAttribute("email")!=null?oauthUser.getAttribute("email").toString():oauthUser.getAttribute("login").toString()+"@gmail.com";
                String picture=oauthUser.getAttribute("avatar_url").toString();
                String name=oauthUser.getAttribute("login").toString();
                String providerUserId=oauthUser.getName();  
                
                user.setName(name);
                user.setEmail(email);
                user.setProfilePic(picture);
                user.setProviderUserId(providerUserId);
                user.setProviders(Providers.GITHUB);
                user.setAbout("This account created using GitHub");

            }
            else{
                logger.info("OAuthenticationSuccessHandler: Unknown Provider !");
            }

                 User user2=repo.findByEmail(user.getEmail()).orElse(null);
                 if(user2==null){
                     repo.save(user);
                     logger.info("User Saved");
                     
                 }
            // Here can also use Response to Redircet to the perticuler page 
            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
        }

}
