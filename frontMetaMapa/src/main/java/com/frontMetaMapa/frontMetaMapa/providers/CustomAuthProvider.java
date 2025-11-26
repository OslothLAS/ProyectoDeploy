package com.frontMetaMapa.frontMetaMapa.providers;

import com.frontMetaMapa.frontMetaMapa.exceptions.RateLimitException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.AuthResponseDTO;
import com.frontMetaMapa.frontMetaMapa.services.LoginApiService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final LoginApiService loginApiService;

    public CustomAuthProvider(LoginApiService loginApiService) {
        this.loginApiService = loginApiService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("🔑 authenticate() llamado para usuario: " + username);

        try {
            AuthResponseDTO authResponse = loginApiService.login(username, password);

            if (authResponse == null) {
                log.warn("Usuario o contraseña inválidos para: {}", username);
                throw new BadCredentialsException("Usuario o contraseña inválidos");
            }

            System.out.println("Usuario autenticado. Guardando tokens en sesión");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            request.getSession().setAttribute("accessToken", authResponse.getAccessToken());
            request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
            request.getSession().setAttribute("username", username);

            // Extraer rol directamente del accessToken
            String rol = extractRoleFromToken(authResponse.getAccessToken());
            System.out.println("Rol obtenido del token: {}"+ rol);

            if (rol == null || rol.isEmpty()) {
                log.warn("No se pudo obtener el rol del usuario: {}", username);
                throw new BadCredentialsException("No se pudo obtener el rol del usuario");
            }

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));

            return new UsernamePasswordAuthenticationToken(username, null, authorities);

        } catch (RateLimitException e) {
            // ✅ CORRECCIÓN CLAVE:
            // Si es error de RateLimit, NO lo conviertas en BadCredentials.
            // Lánzalo tal cual para que el Controller lo reciba y muestre el cartel rojo.
            throw e;

        } catch (RuntimeException e) {
            // Para el resto de errores (conexión, url vacía, etc), sí usa BadCredentials
            System.out.println("💥 Error en authenticate(): " + e);
            throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * Extrae el rol desde el accessToken JWT
     */



    @Value("${jwt.secret.key}")
    private String jwtSecretBase64;

    private String extractRoleFromToken(String accessToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretBase64));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            return claims.get("rol", String.class);
        } catch (Exception e) {
            log.error("Error al decodificar el accessToken: {}", e.getMessage());
            return null;
        }
    }

}
