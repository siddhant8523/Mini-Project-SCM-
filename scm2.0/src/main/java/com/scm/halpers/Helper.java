package com.scm.halpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){
        if (authentication instanceof OAuth2AuthenticationToken){
            //Return Email if user Logged in with OAuth2 like: GOOGLE,GitHub
            var aOAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
            var clientId=aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            System.out.println(clientId);
            var oauth2User=(OAuth2User)authentication.getPrincipal();
            String username="";
            if(clientId.equalsIgnoreCase("google")){
                username=oauth2User.getAttribute("email").toString();
            }
            else if(clientId.equalsIgnoreCase("github")){
            username=oauth2User.getAttribute("email")!=null?oauth2User.getAttribute("email").toString():oauth2User.getAttribute("login").toString()+"@gmail.com";

            }
            return username;
        }
        else{
            // Return Email if user logged in with Local DB credentials
            return authentication.getName();
        }
    }
}
