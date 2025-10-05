package com.usuarios.servicioDeUsuarios.controllers;

import com.usuarios.servicioDeUsuarios.models.entities.Usuario;
import com.usuarios.servicioDeUsuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;


    @PostMapping
    public void crearUsuario(Usuario usuario) {

    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>();
    }

    @GetMapping("/{username}")
    public Usuario obtenerUsuarioPorUsername(@PathVariable String username) {
        return null;
    }
}
