package com.nexa.hr_management.services;

import com.nexa.hr_management.dto.UserRegistrationDto;
import com.nexa.hr_management.model.User;
import com.nexa.hr_management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;


    public RegistrationService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

    }

    @Transactional
    public User registerUser(UserRegistrationDto registrationDto) {
        // Validate input
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(registrationDto.getPassword());
        user.setRole("user"); // Default role
        user.setPhone(registrationDto.getPhone());
        user.setDateOfBirth(registrationDto.getDateOfBirth());

        return userRepository.save(user);
    }
}