package com.nexa.hr_management.controllers;

import com.nexa.hr_management.model.User;
import com.nexa.hr_management.services.UserCreationService;
import com.nexa.hr_management.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final UserService userService;
    private final UserCreationService userCreationService;

    @Autowired
    public RegistrationController(UserService userService, UserCreationService userCreationService) {
        this.userService = userService;
        this.userCreationService = userCreationService;
    }

    @GetMapping
    public String showRegistrationForm() {
        return "pages/register";
    }

    @PostMapping
    public String registerUser(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String dob,
            @RequestParam String phone,
            Model model) {

        logger.debug("Entering registerUser method.");

        long individualUserCreationCount = userCreationService.getIndividualUserCreationCount();
        logger.info("Current individual user creation count: {}", individualUserCreationCount);

        // Basic field validation
        if (isAnyFieldEmpty(firstname, lastname, email, username, password, dob, phone)) {
            model.addAttribute("error", "All fields are required.");
            return "pages/register";
        }

        // Phone number validation
        if (!isValidPhoneNumber(phone)) {
            model.addAttribute("error", "Invalid phone number format. Please use 10-15 digits.");
            return "pages/register";
        }

        // Password validation
        if (!isValidPassword(password)) {
            model.addAttribute("error", "Password must be at least 8 characters with uppercase, lowercase, digit and special character.");
            return "pages/register";
        }

        // Username validation
        if (!isValidUsername(username)) {
            model.addAttribute("error", "Username must be between 4 and 20 characters.");
            return "pages/register";
        }

        // Date parsing
        Date parsedDate = parseDateOfBirth(dob);
        if (parsedDate == null) {
            model.addAttribute("error", "Invalid date format. Please use MM/dd/yy or yyyy-MM-dd.");
            return "pages/register";
        }

        try {
            // Try to register the user using the service
            User registeredUser = userService.registerUser(
                    firstname, lastname, email, username, password, parsedDate, phone);

            logger.info("User registered successfully: {}", username);
            return "redirect:/login?success=User+registered+successfully!";

        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage(), e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "pages/register";
        }
    }

    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10,15}");
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

    private boolean isValidUsername(String username) {
        return username.length() >= 4 && username.length() <= 20;
    }

    private Date parseDateOfBirth(String dob) {
        SimpleDateFormat[] formats = {new SimpleDateFormat("MM/dd/yy"), new SimpleDateFormat("yyyy-MM-dd")};
        for (SimpleDateFormat format : formats) {
            try {
                return format.parse(dob);
            } catch (ParseException ignored) {}
        }
        return null;
    }
}