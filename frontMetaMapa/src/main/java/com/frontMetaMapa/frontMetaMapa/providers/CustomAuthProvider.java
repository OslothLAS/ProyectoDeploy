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
import org.springframework.security.authentication.AuthenticationServiceException;
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

        try {
            AuthResponseDTO authResponse = loginApiService.login(username, password);

            if (authResponse == null) {
                throw new BadCredentialsException("Usuario o contraseña incorrectos");
            }

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            request.getSession().setAttribute("accessToken", authResponse.getAccessToken());
            request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
            request.getSession().setAttribute("username", username);

            String rol = extractRoleFromToken(authResponse.getAccessToken());

            if (rol == null || rol.isEmpty()) {
                log.warn("No se pudo obtener el rol para: {}", username);
                throw new AuthenticationServiceException("Error al obtener permisos del usuario");
            }

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));

            return new UsernamePasswordAuthenticationToken(username, null, authorities);

        } catch (RateLimitException e) {
            throw new org.springframework.security.authentication.AuthenticationServiceException(
                    "Demasiados intentos, espere " + e.getSegundos() + " segundos"
            );

        } catch (BadCredentialsException e) {
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
            throw new org.springframework.security.authentication.AuthenticationServiceException(
                    "Error interno del sistema de autenticación"
            );
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
