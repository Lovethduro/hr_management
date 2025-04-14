package com.nexa.hr_management.services;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RequestCounterService {

    private final AtomicLong count = new AtomicLong(0);

    public void increment() {
        count.incrementAndGet();
        System.out.println("Current request count: " + count.get()); // Add log to track increment
    }

    public long getCount() {
        return count.get();
    }
}
