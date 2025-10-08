package com.usuarios.servicioDeUsuarios.controllers;

import com.usuarios.servicioDeUsuarios.dtos.UsuarioDTO;
import com.usuarios.servicioDeUsuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Long> crearUsuario(@RequestBody UsuarioDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(user));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.findByUsername(username));
    }
}
