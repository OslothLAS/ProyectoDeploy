package com.usuarios.servicioDeUsuarios.config;

import com.usuarios.servicioDeUsuarios.filters.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aplicar a todo el API
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/**");

        // O solo a endpoints criticos
        // registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/agregador/**");
    }
}