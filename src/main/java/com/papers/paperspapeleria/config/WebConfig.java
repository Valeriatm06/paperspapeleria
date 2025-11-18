package com.papers.paperspapeleria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:5173") // 💡 ¡CRÍTICO! Especifica el origen exacto de tu frontend
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Mantener los métodos específicos
            .allowedHeaders("*")
            .allowCredentials(true); // 💡 ¡CRÍTICO! Permite el envío de credenciales (cookies, auth headers)
    }
}