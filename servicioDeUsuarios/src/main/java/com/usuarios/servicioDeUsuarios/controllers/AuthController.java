package com.usuarios.servicioDeUsuarios.controllers;

import com.usuarios.servicioDeUsuarios.dtos.AuthResponseDTO;
import com.usuarios.servicioDeUsuarios.dtos.RefreshRequest;
import com.usuarios.servicioDeUsuarios.dtos.TokenResponse;
import com.usuarios.servicioDeUsuarios.dtos.UserRolesPermissionsDTO;
import com.usuarios.servicioDeUsuarios.exceptions.NotFoundException;
import com.usuarios.servicioDeUsuarios.filters.TokenInfo;
import com.usuarios.servicioDeUsuarios.models.entities.Usuario;
import com.usuarios.servicioDeUsuarios.services.LoginService;
import com.usuarios.servicioDeUsuarios.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> loginApi(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            // Validación básica de credenciales
            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Autenticar usuario usando el LoginService
            Usuario usuario = loginService.autenticarUsuario(username, password);


            // Generar tokens
            String accessToken = loginService.generarAccessToken(username,usuario.getRol().name());
            String refreshToken = loginService.generarRefreshToken(username,usuario.getRol().name());

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            log.info("El usuario {} está logueado. El token generado es {}", username, accessToken);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        try {
            TokenInfo info = JwtUtil.validarToken(request.getRefreshToken());

            String username = info.getUsername();
            String rol = info.getRol();

            // Validar que el token sea de tipo refresh
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getKey())
                    .build()
                    .parseClaimsJws(request.getRefreshToken())
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().build();
            }

            String newAccessToken = JwtUtil.generarAccessToken(username,rol);
            TokenResponse response = new TokenResponse(newAccessToken, request.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/roles-permisos")
    public ResponseEntity<UserRolesPermissionsDTO> getUserRolesAndPermissions(Authentication authentication) {
        try {
            String username = authentication.getName();
            UserRolesPermissionsDTO response = loginService.obtenerRolesYPermisosUsuario(username);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos del usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
