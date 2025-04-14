package com.nexa.hr_management.controllers;

import com.nexa.hr_management.model.Employee;
import com.nexa.hr_management.model.User;
import com.nexa.hr_management.repository.UserRepository;
import com.nexa.hr_management.services.UserCreationService;
import com.nexa.hr_management.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/createUser")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserCreationService userCreationService;

    // Constants
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int PHONE_LENGTH = 10;

    // Constructor with both dependencies
    public UserController(UserRepository userRepository, UserService userService, UserCreationService userCreationService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userCreationService = userCreationService;
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        System.out.println("Deleting user with ID: " + id);
        try {
            userService.deleteUser(id); // Assuming this method is in your UserService class.
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/createUser/display"; // Redirect back to the user list
    }



    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "pages/user"; // user.html under templates
    }

    @PostMapping
    public String createUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("Attempting to save user: " + user);

        userCreationService.incrementAdminUserCreationCount();

        // Password validation
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH || user.getPassword().length() > MAX_PASSWORD_LENGTH) {
            model.addAttribute("passwordError", "Password must be between 8 and 20 characters.");
            return "pages/user"; // Stay on the same page if validation fails
        }

        // Phone validation
        if (user.getPhone().length() != PHONE_LENGTH) {
            model.addAttribute("phoneError", "Phone number must be exactly 10 digits.");
            return "pages/user"; // Stay on the same page if validation fails
        }

        // Check if user already exists
        boolean isTaken = userRepository.existsByEmailOrPhoneOrUsername(
                user.getEmail(), user.getPhone(), user.getUsername());

        if (isTaken) {
            model.addAttribute("error", "Email, Phone, or Username already exists.");
            return "pages/user"; // Stay on the same page if user already exists
        }

        // Default role
        user.setRole("user");

        // Save user
        userRepository.save(user);
        System.out.println("User saved: " + user);

        // Add a success message to be displayed after redirection
        redirectAttributes.addFlashAttribute("success", "User created successfully");

        // Redirect to the admin dashboard
        return "redirect:/admin";  // Ensure this path is mapped correctly
    }


    @GetMapping("/api/newUsersToday")
    @ResponseBody
    public long getNewUsersToday() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return userRepository.countUsersRegisteredToday(startOfDay, endOfDay);
    }

    @GetMapping("/display")
    public String displayUsers(
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        try {
            List<User> users;
            List<String> excludedRoles = Arrays.asList("admin", "hrm");

            if (search != null && !search.trim().isEmpty()) {
                users = userService.searchUsers(search);
                // Filter out admin and hrm roles from search results
                users = users.stream()
                        .filter(user -> !excludedRoles.contains(user.getRole()))
                        .collect(Collectors.toList());
                model.addAttribute("search", search);
            } else {
                // Directly use the repository if you can't modify the service
                users = userRepository.findByRoleNotIn(excludedRoles);
            }

            users = users.stream()
                    .filter(user -> !excludedRoles.contains(user.getRole()))
                    .sorted(Comparator.comparing(User::getId)) // Sort by ID ascending
                    // For descending: .sorted(Comparator.comparing(User::getId).reversed())
                    .collect(Collectors.toList());

            model.addAttribute("users", users);
            if (search != null && !search.trim().isEmpty()) {
                model.addAttribute("search", search);
            }


            model.addAttribute("users", users);

        } catch (Exception e) {
            model.addAttribute("error", "Error fetching users.");
            model.addAttribute("errorMessage", "Error retrieving users: " + e.getMessage());
        }
        return "pages/displayUser";
    }


    // For AJAX delete requests
    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUserApi(@PathVariable Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"User deleted successfully\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"User not found\"}");
            }
        } catch (Exception e) {
            logger.warning("Error deleting user via API: " + e.getMessage());
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Error deleting user\"}");
        }
    }
}