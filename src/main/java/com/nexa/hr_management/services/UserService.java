package com.nexa.hr_management.services;

import com.nexa.hr_management.model.User;
import com.nexa.hr_management.repository.UserRepository;
import com.nexa.hr_management.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserCreationService userCreationService;

    public UserService(UserRepository userRepository, UserCreationService userCreationService) {
        this.userRepository = userRepository;
        this.userCreationService = userCreationService;
    }

    public boolean existsByEmailOrPhoneOrUsername(String email, String phone, String username) {
        return userRepository.existsByEmailOrPhoneOrUsername(email, phone, username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public User registerUser(String firstName, String lastName, String email, String username, String password, Date dob, String phone) {
        logger.debug("Starting registration process for user: {}", username);

        // Check if the user already exists by email, phone, or username
        logger.debug("Checking if user already exists with email: {}, phone: {}, username: {}", email, phone, username);
        if (existsByEmailOrPhoneOrUsername(email, phone, username)) {
            logger.warn("Registration failed: User with these credentials already exists");
            throw new IllegalArgumentException("User with these credentials already exists");
        }

        // Create new user object
        logger.debug("Creating new user object for: {}", username);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUsername(username);
        user.setPassword(password); // Store password in plain text

        // Convert Date to LocalDate and set it in the user object
        logger.debug("Parsing date of birth: {}", dob);
        LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        user.setDateOfBirth(birthDate);

        // Set the user's role
        user.setRole("user");

        // Log user information before saving (excluding sensitive data like password)
        logger.debug("User to be saved: {}", user);

        // Save user to the database
        logger.info("Saving user to database: {}", username);
        User savedUser = userRepository.save(user);

        // Log successful user creation
        logger.info("User saved successfully with ID: {}", savedUser.getId());

        // Log the individual user creation count (optional)
        long individualUserCreationCount = userCreationService.getIndividualUserCreationCount();
        logger.debug("Current individual user creation count: {}", individualUserCreationCount);

        // Return the saved user
        return savedUser;
    }


    @Transactional
    public void updateProfileImage(String username, MultipartFile file) throws IOException {
        User user = findByUsername(username);
        user.setProfileImage(file.getBytes());
        userRepository.save(user);
    }

    public byte[] getProfileImage(String username) {
        return findByUsername(username).getProfileImage();
    }

    public List<User> searchUsers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.searchUsers(searchTerm);
    }

    // Other service methods
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }


    public List<User> getAllUsersExceptRoles(List<String> excludedRoles) {
        return userRepository.findByRoleNotIn(excludedRoles);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}