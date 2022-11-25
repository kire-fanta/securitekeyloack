package com.example.demo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Map;

@RestController
public class RedirectionController {


        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

        /*@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        @RequestMapping("/**")
        public String getUser()
        {
            return "Welcome User";
        }

        @PreAuthorize("hasRole('ADMIN')")
        @RequestMapping("/admin")
        public String getAdmin()
        {
            return "Welcome Admin";
        }*/

        @RequestMapping("/**")
        private StringBuffer getOauth2LoginInfo(Principal user){

                StringBuffer protectedInfo = new StringBuffer();

                OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
                OAuth2AuthorizedClient authClient = this.oAuth2AuthorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
                if(authToken.isAuthenticated()){

                        Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

                        String userToken = authClient.getAccessToken().getTokenValue();
                        protectedInfo.append("Welcome, " + userAttributes.get("name")+"<br><br>");
                        protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
                        protectedInfo.append("Access Token: " + userToken+"<br><br>");
                }
                else{
                        protectedInfo.append("NA");
                }
                return protectedInfo;
        }

}
