package com.usuarios.servicioDeUsuarios.utils;

import com.usuarios.servicioDeUsuarios.dtos.UsuarioDTO;
import com.usuarios.servicioDeUsuarios.models.entities.Usuario;

public class UsuarioUtil {
    public static UsuarioDTO usuarioToDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getUsername(), usuario.getContrasenia(), usuario.getNombre(), usuario.getApellido()
                , usuario.getFechaNacimiento(), usuario.getRol());
    }

    public static Usuario usuarioDTOToEntity(UsuarioDTO usuarioDTO) {
        return new Usuario(usuarioDTO.getUsername(), usuarioDTO.getContrasenia() ,usuarioDTO.getNombre(), usuarioDTO.getApellido(),
                usuarioDTO.getFechaNacimiento(), usuarioDTO.getRol());
    }
}
