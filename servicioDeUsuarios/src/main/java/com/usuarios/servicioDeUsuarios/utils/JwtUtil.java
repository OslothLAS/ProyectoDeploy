package com.usuarios.servicioDeUsuarios.utils;

import com.usuarios.servicioDeUsuarios.filters.TokenInfo;
import com.usuarios.servicioDeUsuarios.models.repositories.IUsuarioRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {


    @Getter
    @Setter //para test
    private static Key key;



    public JwtUtil(@Value("${jwt.secret.key}") String secretBase64) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64)); // Puedes necesitar Decoders.BASE64.decode() dependiendo de cómo la generes
    }

    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

    public static String generarAccessToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("servicio-de-usuarios")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .claim("rol", rol)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public static String generarRefreshToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("servicio-de-usuarios")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .claim("type", "refresh") // diferenciamos refresh del access
                .claim("rol", rol)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public static TokenInfo validarToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        String rol = claims.get("rol", String.class);

        return new TokenInfo(username, rol);
    }
}
