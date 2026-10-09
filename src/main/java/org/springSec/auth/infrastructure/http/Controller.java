package org.springSec.auth.infrastructure.http;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping 
public class Controller {
    
    @GetMapping 
    public String hello(@AuthenticationPrincipal UserDetails user) {
        return "Hello world" + user.getUsername();
    }

    @GetMapping("/user")
    @PreAuthorize ("hasRole('roles')")
    public String userEndpoint() {
        return "Hello USERNAME";
    }

    @GetMapping ("/brand")
    @PreAuthorize ("hasRole('BRAND')")
    public String adminEndpoint() {
        return "Hello Brand";
    }
}
