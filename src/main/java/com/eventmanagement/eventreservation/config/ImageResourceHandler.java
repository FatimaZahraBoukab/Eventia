package com.eventmanagement.eventreservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration pour servir les images uploadées
 */
@Configuration
public class ImageResourceHandler implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Permettre l'accès aux images uploadées
        registry.addResourceHandler("/uploads/events/**")
                .addResourceLocations("file:uploads/events/");
    }
}