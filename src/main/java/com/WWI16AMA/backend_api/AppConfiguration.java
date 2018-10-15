package com.WWI16AMA.backend_api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AppConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {

            String[] origins = {"http://localhost", "http://localhost:4200",
                    "https://localhost", "https://localhost:4200"};
            String[] methods = {"GET", "POST", "PUT", "DELETE"};

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods(methods);
            }
        };
    }
}
