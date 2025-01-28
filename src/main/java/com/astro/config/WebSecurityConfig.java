package com.astro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {



     registry.addMapping("/**").allowedMethods("*");
      /*  registry.addMapping("/**") // Allows all endpoints
                .allowedOrigins("http://localhost:5001")  // Change this to match your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // Specify allowed methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow cookies if necessary

       */
    }

}
