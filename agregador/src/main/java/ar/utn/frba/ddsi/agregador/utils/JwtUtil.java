package ar.utn.frba.ddsi.agregador.utils;


import ar.utn.frba.ddsi.agregador.dtos.input.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    @Getter
    private static Key key;

    public JwtUtil(@Value("${jwt.secret.key}") String secretBase64) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
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
