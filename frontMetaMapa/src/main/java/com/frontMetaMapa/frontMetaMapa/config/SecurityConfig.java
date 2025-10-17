package com.frontMetaMapa.frontMetaMapa.config;

import com.frontMetaMapa.frontMetaMapa.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomSuccessHandler successHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/",
                                "/buscador-colecciones",
                                "/register",
                                "/visualizador",
                                "/visualizador/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/buscador-hechos",
                                "/buscador-hechos/**",
                                "/buscador-colecciones/**",
                                "/colecciones/**",
                                "/error/**",     // 👈 importante
                                "/404",
                                "/solicitar-eliminacion",
                                "/solicitar-eliminacion/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler) // 👈 acá reemplazamos defaultSuccessUrl
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("❌ Acceso no autorizado a: " + request.getRequestURI());
                            response.sendRedirect("/login?unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/403")
                        )
                );

        return http.build();
    }
}