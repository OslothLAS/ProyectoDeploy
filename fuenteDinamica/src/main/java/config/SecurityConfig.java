package config;

import ar.utn.ba.ddsi.fuenteDinamica.filters.JwtAuthenticationFilter;
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