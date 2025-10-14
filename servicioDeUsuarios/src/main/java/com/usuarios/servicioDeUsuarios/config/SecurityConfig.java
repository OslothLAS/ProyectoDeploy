package com.usuarios.servicioDeUsuarios.config;

import com.usuarios.servicioDeUsuarios.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",         // El endpoint JSON/YAML de OpenAPI
            "/swagger-ui/**",          // Todos los recursos estáticos de Swagger UI
            "/swagger-ui.html"         // La página principal
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("=== CONFIGURANDO SECURITY (CORREGIDO) ===");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(SWAGGER_WHITELIST).permitAll();

                    // CAMBIO CLAVE: Se agregó "/**" para permitir el acceso a sub-rutas como /api/users/1
                    auth.requestMatchers("/api/auth/**", "/api/auth/refresh", "/api/users/**").permitAll();

                    auth.requestMatchers("/api/auth/user/roles-permisos").authenticated();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}