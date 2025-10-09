package ar.utn.frba.ddsi.agregador.filters;

import ar.utn.frba.ddsi.agregador.dtos.input.TokenInfo;
import ar.utn.frba.ddsi.agregador.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public JwtAuthenticationFilter() {
        System.out.println("AGREGADOR - JwtAuthenticationFilter CONSTRUCTOR ejecutado");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("FILTRO EJECUTADO - URL: " + request.getRequestURL());

        String header = request.getHeader("Authorization");
        System.out.println("🔍 Authorization Header: " + header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("Token recibido: " + token);

            try {
                // VALIDACIÓN REAL DEL TOKEN
                TokenInfo tokenInfo = JwtUtil.validarToken(token);
                System.out.println("Token VÁLIDO - Usuario: " + tokenInfo.getUsername());
                System.out.println("Rol: " + tokenInfo.getRol());

                // Configurar autenticación CON LOS DATOS REALES DEL TOKEN
                List<GrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + tokenInfo.getRol())
                );
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(tokenInfo.getUsername(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                System.out.println("Token INVÁLIDO: " + e.getMessage());
                response.setStatus(401);
                response.getWriter().write("Token inválido: " + e.getMessage());
                return;
            }
        } else {
            System.out.println("No hay token Bearer");
            // NO configures autenticación - dejará SecurityContext vacío
        }

        filterChain.doFilter(request, response);
    }
}