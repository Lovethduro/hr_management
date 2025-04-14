package com.nexa.hr_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegalController {

    @GetMapping("/privacy-policy")
    public String privacyPolicy(Model model) {
        // Add any dynamic content you want to display
        model.addAttribute("lastUpdated", "June 15, 2023");
        return "forward:/privacyPolicy.html";
    }
}