package com.nexa.hr_management.controllers;

import com.nexa.hr_management.services.RequestCounterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final RequestCounterService requestCounterService;

    // Constructor injection for RequestCounterService
    public StatsController(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @GetMapping("/total-requests")
    public Map<String, Long> getTotalRequests() {
        // Access the count via RequestCounterService
        return Map.of("count", requestCounterService.getCount());
    }
}
