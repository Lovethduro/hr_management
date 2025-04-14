package com.nexa.hr_management.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.stream.Collectors;

@Controller
public class DebugController {

    @GetMapping("/debug/roles")
    @ResponseBody
    public String debugRoles(Authentication authentication) {
        if (authentication == null) {
            return "Not authenticated";
        }

        return "User: " + authentication.getName() + ", Roles: " +
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(", "));
    }
}
