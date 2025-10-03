package com.usuarios.servicioDeUsuarios.utils;

import org.junit.jupiter.api.Test;

import static com.usuarios.servicioDeUsuarios.utils.JwtUtil.generarAccessToken;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generarAccessTokenTest() {
        String accessToken = generarAccessToken("fran456", "CONTRIBUYENTE");
        System.out.println(accessToken);
    }
}