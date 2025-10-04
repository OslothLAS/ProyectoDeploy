package ar.utn.ba.ddsi.fuenteDinamica.utils;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import java.security.Key;

public class JwtUtil {
    @Getter
    private static Key key;

    public static void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("0C44BmsWJnuBF+jBfXPfyn6SL/6KCf/6KILHM6NgByGhgjvRQWW6Fjr8hMR2S087wjOPSSKE3lyRoKd/azokSg=="));
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
