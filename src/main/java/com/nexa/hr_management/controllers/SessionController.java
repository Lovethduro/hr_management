package com.nexa.hr_management.controllers;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionRegistry sessionRegistry;

    public SessionController(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/active-count")
    public Map<String, Integer> getActiveSessionCount() {
        int activeCount = sessionRegistry.getAllPrincipals().size();
        return Map.of("count", activeCount);
    }
}