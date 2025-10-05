package com.usuarios.servicioDeUsuarios.utils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JwtUtilTest {
    @BeforeAll
    static void setup() {
        // Simulamos lo que haría el constructor del @Component
        JwtUtil.setKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                "0C44BmsWJnuBF+jBfXPfyn6SL/6KCf/6KILHM6NgByGhgjvRQWW6Fjr8hMR2S087wjOPSSKE3lyRoKd/azokSg=="
        )));
    }

    @Test
    void generarAccessTokenTest() {
        String accessToken = JwtUtil.generarAccessToken("fran456", "CONTRIBUYENTE");
        System.out.println(accessToken);
        Assertions.assertNotNull(accessToken);
    }
}