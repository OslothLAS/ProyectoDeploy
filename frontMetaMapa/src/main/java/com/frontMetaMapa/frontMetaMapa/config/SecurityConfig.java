package com.frontMetaMapa.frontMetaMapa.config;

import com.frontMetaMapa.frontMetaMapa.providers.CustomAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Autowired
    private DenyIpFilter denyIpFilter;  // ⬅️ inyectamos tu filtro

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
                // ⬅️ AÑADIR EL FILTRO DE DENY LIST ANTES DE AUTENTICACIÓN
                .addFilterBefore(denyIpFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth

                        // RUTAS PÚBLICAS
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
                        // SOLO MANEJA NO AUTENTICADO
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login?unauthorized");
                        })

                        // SI ESTÁ AUTENTICADO PERO SIN PERMISOS (403)
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/403")
                        )
                );

        return http.build();
    }
}
