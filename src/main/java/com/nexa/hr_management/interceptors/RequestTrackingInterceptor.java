package com.nexa.hr_management.interceptors;

import com.nexa.hr_management.services.RequestCounterService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestTrackingInterceptor implements HandlerInterceptor {

    private final RequestCounterService requestCounterService;

    // Cache to store last request time for each URI
    private Map<String, Long> requestTimestamps = new HashMap<>();

    // Time interval (in milliseconds) before a new request for the same URL is counted again (30 seconds)
    private static final long REQUEST_INTERVAL = 30 * 1000; // 30 seconds

    public RequestTrackingInterceptor(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();

        // Only count requests that are not static resources or API endpoints
        if (isValidRequest(requestUri)) {
            // Check if we should count this request (i.e., if enough time has passed since the last one)
            if (shouldCountRequest(requestUri)) {
                requestCounterService.increment();
                System.out.println("Request intercepted: " + requestUri);

                // Store the current time as the last request time for this URI
                storeRequestTime(requestUri);
            }
        }

        return true; // Continue processing the request
    }

    private boolean isValidRequest(String requestUri) {
        // Exclude common static resources and API endpoints
        if (requestUri.endsWith(".js") || requestUri.endsWith(".css") || requestUri.endsWith(".png")
                || requestUri.endsWith(".jpg") || requestUri.endsWith(".jpeg") || requestUri.endsWith(".mp4")
                || requestUri.startsWith("/error") || requestUri.equals("/profile-image") || requestUri.startsWith("/api/")) {
            return false;
        }
        return true;
    }

    private boolean shouldCountRequest(String requestUri) {
        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = requestTimestamps.get(requestUri);

        // If it's the first request or more than the defined interval has passed since the last one, count the request
        return lastRequestTime == null || currentTime - lastRequestTime > REQUEST_INTERVAL;
    }

    private void storeRequestTime(String requestUri) {
        long currentTime = System.currentTimeMillis();
        requestTimestamps.put(requestUri, currentTime);
    }
}
