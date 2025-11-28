package com.frontMetaMapa.frontMetaMapa.config;

import com.frontMetaMapa.frontMetaMapa.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                                "/register",
                                "/",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/visualizador/**",
                                "/buscador-hechos/**",
                                "/buscador-colecciones/**",
                                "/colecciones/**",
                                "/solicitar-eliminacion/**",
                                "/hechoColeccion/**",
                                "/error",
                                "/error/**",
                                "/404",
                                "/403"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/subirCsv").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
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
                            String uri = request.getRequestURI();
                            System.out.println("❌ Acceso no autorizado a: " + uri);

                            // Si el usuario intenta acceder a una URL protegida
                            // lo llevamos al login. Pero si el error es 404, no.
                            if (uri.startsWith("/error") || uri.equals("/404") || uri.equals("/403")) {
                                response.sendRedirect("/404");
                            } else {
                                response.sendRedirect("/login?unauthorized");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/403")
                        )
                );

        // ❗️Clave: el endpoint /error se maneja fuera de los filtros de autenticación
        http.securityMatcher("/**");

        return http.build();
    }
}