package com.nexa.hr_management.services;

import com.nexa.hr_management.model.User;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRegistry sessionRegistry;

    public SessionService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public int getActiveSessionCount() {
        return (int) sessionRegistry.getAllPrincipals().stream()
                .flatMap(principal -> sessionRegistry.getAllSessions(principal, false).stream())
                .count();
    }

    public List<String> getActiveUsernames() {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof User) // ensure it's your User type
                .map(principal -> {
                    // Safely cast the principal to User and retrieve username
                    return ((User) principal).getUsername();
                })
                .collect(Collectors.toList());
    }



}