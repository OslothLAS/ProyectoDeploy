package com.usuarios.servicioDeUsuarios.utils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.Key;

import static com.usuarios.servicioDeUsuarios.utils.JwtUtil.generarAccessToken;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    @BeforeAll
    static void setup() {
        // Clave secreta de ejemplo, debe ser suficientemente larga (mínimo 256 bits para HS256)
        String secretBase64 = "0C44BmsWJnuBF+jBfXPfyn6SL/6KCf/6KILHM6NgByGhgjvRQWW6Fjr8hMR2S087wjOPSSKE3lyRoKd/azokSg==";
        byte[] bytes = Decoders.BASE64.decode(secretBase64);
        System.out.println(bytes.length); // debe mostrar 64 para HS512
        JwtUtil.init();
    }

    @Test
    void generarAccessTokenTest() {
        String accessToken = generarAccessToken("fran456", "CONTRIBUYENTE");
        System.out.println(accessToken);
        var hola = "gola";
        Assertions.assertEquals(hola, "gola");
    }
}