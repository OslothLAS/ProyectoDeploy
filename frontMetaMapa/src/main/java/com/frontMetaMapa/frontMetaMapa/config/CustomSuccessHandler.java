package com.frontMetaMapa.frontMetaMapa.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            System.out.println("✅ Rol detectado: " + role);

            if (role.equalsIgnoreCase("ROLE_CONTRIBUYENTE") || role.equalsIgnoreCase("CONTRIBUYENTE")) {
                response.sendRedirect("/contribuyente");
                return;
            } else if (role.equalsIgnoreCase("ROLE_ADMIN") || role.equalsIgnoreCase("ADMIN")) {
                response.sendRedirect("/administrador");
                return;
            }
        }

        // Si no coincide ningún rol, redirigir a login
        response.sendRedirect("/login?sinRol");
    }
}
