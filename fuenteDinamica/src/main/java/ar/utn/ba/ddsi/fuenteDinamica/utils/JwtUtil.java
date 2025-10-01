package ar.utn.ba.ddsi.fuenteDinamica.utils;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import java.security.Key;

public class JwtUtil {
    @Getter
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static void init(String secretBase64) {
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
