package com.nexa.hr_management.services;

import com.nexa.hr_management.model.User;
import com.nexa.hr_management.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserCreationService {

    private final UserRepository userRepository;

    // Use AtomicLong for thread-safe counting
    private final AtomicLong adminUserCreationCount = new AtomicLong(0);
    private final AtomicLong individualUserCreationCount = new AtomicLong(0);

    private LocalDateTime adminLastResetTime = LocalDateTime.now();
    private LocalDateTime individualLastResetTime = LocalDateTime.now();

    // Constructor injection for cleaner design and testability
    public UserCreationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Increments count for admin user creation
    public void incrementAdminUserCreationCount() {
        if (ChronoUnit.HOURS.between(adminLastResetTime, LocalDateTime.now()) >= 24) {
            resetAdminUserCreationCount();
        }
        adminUserCreationCount.incrementAndGet();
    }

    // Increments count for individual user creation
    public void incrementIndividualUserCreationCount() {
        if (ChronoUnit.HOURS.between(individualLastResetTime, LocalDateTime.now()) >= 24) {
            resetIndividualUserCreationCount();
        }
        individualUserCreationCount.incrementAndGet();
    }

    // Scheduled reset for admin count at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetAdminUserCreationCount() {
        adminUserCreationCount.set(0);
        adminLastResetTime = LocalDateTime.now();
    }

    // Scheduled reset for individual count at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetIndividualUserCreationCount() {
        individualUserCreationCount.set(0);
        individualLastResetTime = LocalDateTime.now();
    }

    // Get admin user creation count with auto reset if needed
    public long getAdminUserCreationCount() {
        if (ChronoUnit.HOURS.between(adminLastResetTime, LocalDateTime.now()) >= 24) {
            resetAdminUserCreationCount();
        }
        return adminUserCreationCount.get();
    }

    // Get individual user creation count with auto reset if needed
    public long getIndividualUserCreationCount() {
        if (ChronoUnit.HOURS.between(individualLastResetTime, LocalDateTime.now()) >= 24) {
            resetIndividualUserCreationCount();
        }
        return individualUserCreationCount.get();
    }

    // Admin creates user
    public User createUserByAdmin(User user, Long adminId) {
        user = userRepository.save(user);
        incrementAdminUserCreationCount();
        return user;
    }

    // Individual creates user
    public User createUserByIndividual(User user) {
        user = userRepository.save(user);
        incrementIndividualUserCreationCount();
        return user;
    }
}
