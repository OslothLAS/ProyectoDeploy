package ar.utn.frba.ddsi.agregador.config;

import ar.utn.frba.ddsi.agregador.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        System.out.println("AGREGADOR - SecurityConfig CONSTRUCTOR ejecutado");
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/solicitudes", "/hechos").permitAll()                // ⬅️ públicos
                        .requestMatchers(HttpMethod.GET, "/hechos/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/colecciones").permitAll()
                        .requestMatchers(HttpMethod.GET, "/colecciones/*").permitAll()           // ⬅️ GET /colecciones
                        .requestMatchers(HttpMethod.GET, "/colecciones/*/hechos").permitAll()  // ⬅️ GET /colecciones/{id}/hechos
                        .anyRequest().authenticated()                                          // ⬅️ el resto con token
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}