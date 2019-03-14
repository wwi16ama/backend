package com.WWI16AMA.backend_api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    final String[] origins = {"http://localhost", "http://localhost:4200",
            "https://localhost", "https://localhost:4200",
            "http://wwi16ama.feste-ip.net", "https://wwi16ama.feste-ip.net"};
    final String[] methods = {"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods(methods);
    }

}
