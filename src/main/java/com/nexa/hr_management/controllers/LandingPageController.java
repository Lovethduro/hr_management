package com.nexa.hr_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPageController {

    @GetMapping("/landing")
    public String showLandingPage() {
        return "pages/landing"; // This resolves to src/main/resources/templates/pages/index.html
    }
}