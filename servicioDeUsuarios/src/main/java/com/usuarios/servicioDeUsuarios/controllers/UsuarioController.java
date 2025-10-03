package com.usuarios.servicioDeUsuarios.controllers;

import com.usuarios.servicioDeUsuarios.models.entities.Usuario;
import com.usuarios.servicioDeUsuarios.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;


    @GetMapping
    @PreAuthorize("anyHashRole('ADMIN')")
    public void crearUsuario(Usuario usuario) {

    }

}
