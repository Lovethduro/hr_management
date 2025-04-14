package com.nexa.hr_management.controllers;

import com.nexa.hr_management.model.Employee;
import com.nexa.hr_management.model.User;
import com.nexa.hr_management.services.EmployeeServiceImpl;
import com.nexa.hr_management.services.UserCreationService;
import com.nexa.hr_management.services.UserService;
import com.nexa.hr_management.repository.EmployeeRepository;  // Import the repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserCreationService userCreationService;
    private final EmployeeRepository employeeRepository;  // Inject EmployeeRepository
    private final EmployeeServiceImpl employeeServiceImpl;

    // Constructor injection
    @Autowired
    public AdminController(UserService userService, UserCreationService userCreationService, EmployeeRepository employeeRepository, EmployeeServiceImpl employeeServiceImpl) {
        this.userService = userService;
        this.userCreationService = userCreationService;
        this.employeeRepository = employeeRepository;  // Initialize repository
        this.employeeServiceImpl = employeeServiceImpl;
    }

    // Helper method to encode image to Base64
    private String encodeImageToBase64(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(imageData);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model, Authentication authentication) {
        try {
            // Retrieve admin's details
            User admin = userService.findByUsername(authentication.getName());

            // Add admin details to the model
            model.addAttribute("adminFirstName", admin.getFirstName());
            model.addAttribute("adminLastName", admin.getLastName());

            // Add profile image if it exists
            if (admin.getProfileImage() != null) {
                model.addAttribute("adminImage", encodeImageToBase64(admin.getProfileImage()));
            }

            // Get total employee count
            long totalEmployees = employeeServiceImpl.getEmployeeCount();
            model.addAttribute("totalEmployees", totalEmployees);

            long adminCreationCount = userCreationService.getAdminUserCreationCount();
            model.addAttribute("adminCreationCount", adminCreationCount); // Add to model



            return "pages/admin";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }

// Remove the redundant @GetMapping("/dashboard") method
}