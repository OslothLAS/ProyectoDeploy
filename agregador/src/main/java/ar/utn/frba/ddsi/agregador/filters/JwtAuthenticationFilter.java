package ar.utn.frba.ddsi.agregador.filters;


import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpHeaders;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String token = getTokenFromRequest(request);
        final String username;

        if (token != null) {
            filterChain.doFilter(request, response);
            return;
        }
        username = getUserNameFromToken();

        filterChain.doFilter(request, response); // 🔹 deja pasar siempre
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Autorizacion");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false; // nunca bloquea
    }
}