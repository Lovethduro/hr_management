package com.nexa.hr_management.controllers;

import com.nexa.hr_management.model.User;
import com.nexa.hr_management.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/editUser")
public class EditUserController {

    private final UserRepository userRepository;

    public EditUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Show the edit form with user data
    @GetMapping("/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "pages/editUser";  // Your edit form template
        } else {
            // If user not found, redirect to user list
            model.addAttribute("error", "User not found");
            return "redirect:/createUser/display";
        }
    }

    // Handle update of user data
    @PostMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Update only the fields that are provided (non-null and not empty)
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                existingUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                existingUser.setLastName(user.getLastName());
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                existingUser.setUsername(user.getUsername());
            }
            if (user.getDateOfBirth() != null) {
                existingUser.setDateOfBirth(user.getDateOfBirth());
            }
            if (user.getRole() != null && !user.getRole().isEmpty()) {
                existingUser.setRole(user.getRole());
            }
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                existingUser.setPhone(user.getPhone());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());  // Ensure validation if password is provided
            }

            // Save the updated user
            userRepository.save(existingUser);

            redirectAttributes.addFlashAttribute("success", "User updated successfully");
            return "redirect:/createUser/display";  // Redirect to the user list or another page after updating
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/createUser/display";  // Redirect if user not found
        }
    }

}
