package com.prosigmaka.catra.diglett.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:clients.properties")
public class WebConfig implements WebMvcConfigurer
{
    @Value("${allowedHosts}")
    private String allowedHosts;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowedHosts.split(",")).allowedMethods("POST", "GET", "DELETE", "PUT");;

    }
}
