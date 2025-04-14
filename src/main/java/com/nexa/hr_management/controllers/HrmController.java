package com.nexa.hr_management.controllers;

import com.nexa.hr_management.model.User;
import com.nexa.hr_management.repository.EmployeeRepository;
import com.nexa.hr_management.services.EmployeeServiceImpl;
import com.nexa.hr_management.services.UserCreationService;
import com.nexa.hr_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Map;

@Controller
@RequestMapping("/hrm")
public class HrmController {

    private final UserService userService;
    private final UserCreationService userCreationService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public HrmController(UserService userService, UserCreationService userCreationService,
                         EmployeeRepository employeeRepository, EmployeeServiceImpl employeeServiceImpl) {
        this.userService = userService;
        this.userCreationService = userCreationService;
        this.employeeRepository = employeeRepository;
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
    @PreAuthorize("hasRole('HRM')")
    public String hrmDashboard(Model model, Authentication authentication) {
        try {
            // Retrieve HRM's details
            User hrm = userService.findByUsername(authentication.getName());

            // Add HRM details to the model
            model.addAttribute("hrmFirstName", hrm.getFirstName());
            model.addAttribute("hrmLastName", hrm.getLastName());

            // Add profile image if it exists
            if (hrm.getProfileImage() != null) {
                model.addAttribute("hrmImage", encodeImageToBase64(hrm.getProfileImage()));
            }

            // Get total employee count - if needed for the HRM dashboard
            long totalEmployees = employeeServiceImpl.getEmployeeCount();
            model.addAttribute("totalEmployees", totalEmployees);

            // Get all department counts as a map
            Map<String, Long> departmentCounts = employeeServiceImpl.getAllDepartmentCounts();
            model.addAttribute("departmentCounts", departmentCounts);

            return "pages/hrm";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }
}