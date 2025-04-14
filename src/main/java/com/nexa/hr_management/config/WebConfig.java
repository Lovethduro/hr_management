package com.nexa.hr_management.config;

import com.nexa.hr_management.interceptors.RequestTrackingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestTrackingInterceptor requestTrackingInterceptor;

    // Constructor injection for the interceptor
    public WebConfig(RequestTrackingInterceptor requestTrackingInterceptor) {
        this.requestTrackingInterceptor = requestTrackingInterceptor;
    }

    // Register the interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTrackingInterceptor)
                .addPathPatterns("/**"); // Apply to all paths (you can modify this to specific paths)
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/privacy-policy")
                .addResourceLocations("classpath:/static/privacyPolicy.html");
    }
}
