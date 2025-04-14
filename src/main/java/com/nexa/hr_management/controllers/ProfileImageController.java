package com.nexa.hr_management.controllers;

import com.nexa.hr_management.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ProfileImageController {

    private final UserService userService;

    public ProfileImageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile-image")
    public ResponseEntity<byte[]> getProfileImage(Authentication authentication) {
        byte[] imageBytes = userService.getProfileImage(authentication.getName());

        if (imageBytes == null || imageBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG
                .body(imageBytes);
    }

    @PostMapping("/upload-profile-image")
    public String uploadProfileImage(@RequestParam("image") MultipartFile file,
                                     Authentication authentication) throws IOException {
        userService.updateProfileImage(authentication.getName(), file);
        return "redirect:/admin/dashboard"; // Adjust redirect as needed
    }
}