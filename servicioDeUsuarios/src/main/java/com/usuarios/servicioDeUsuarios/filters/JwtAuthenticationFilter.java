package com.usuarios.servicioDeUsuarios.filters;

import com.usuarios.servicioDeUsuarios.models.entities.Rol;
import com.usuarios.servicioDeUsuarios.models.repositories.IUsuarioRepository;
import com.usuarios.servicioDeUsuarios.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private IUsuarioRepository usuarioRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                TokenInfo info = JwtUtil.validarToken(token);
                String username = info.getUsername();

                // Buscar usuario
                var usuarioOpt = usuarioRepository.findByUsername(username);

                if (usuarioOpt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                    return;
                }

                var usuario = usuarioOpt.get();
                Rol rol = usuario.getRol();

                var authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + rol.name())
                );

                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno en autenticación");
                return;
            }
        } else {
            System.out.println("No hay token de autorización");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // No aplicar el filtro JWT solo a los endpoints públicos de autenticación
        return path.equals("/api/auth") || path.equals("/api/auth/refresh") || path.equals("/api/users");
    }
}