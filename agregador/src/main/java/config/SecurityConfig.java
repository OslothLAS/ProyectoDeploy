package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // deshabilita CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   // permite todo
                )
                .httpBasic(basic -> basic.disable())   // deshabilita basic auth
                .formLogin(login -> login.disable());  // deshabilita login form

        return http.build();
    }
}