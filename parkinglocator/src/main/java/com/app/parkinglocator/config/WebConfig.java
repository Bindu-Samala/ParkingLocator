package com.app.parkinglocator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Apply CORS to all URLs
            .allowedOrigins("http://localhost:4200")  // Allow requests from Angular app running on this URL
            .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow these HTTP methods
            .allowedHeaders("*")  // Allow all headers
            .allowCredentials(true)  // Allow credentials (cookies, HTTP authentication)
            .maxAge(3600);  // Cache preflight response for 1 hour
    }
}

