package com.usuarios.servicioDeUsuarios.controllers;

import com.usuarios.servicioDeUsuarios.dtos.UsuarioDTO;
import com.usuarios.servicioDeUsuarios.models.entities.Usuario;
import com.usuarios.servicioDeUsuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.usuarios.servicioDeUsuarios.utils.UsuarioUtil.usuarioToDTO;

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

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(usuarioService.findByUsername(username));
    }

    @GetMapping("{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuarioToDTO(usuario));
    }


}
