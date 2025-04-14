package com.nexa.hr_management.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model,
            @RequestParam(required = false) String username,  // Make it optional
            HttpServletRequest request) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }

        // Optionally add the username if it exists (this is just for demonstration, adjust based on your actual needs)
        if (username != null) {
            model.addAttribute("username", username);
        }

        return "pages/login"; // Returning the login view
    }
}
