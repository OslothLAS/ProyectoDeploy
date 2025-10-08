package com.usuarios.servicioDeUsuarios.services;

import com.usuarios.servicioDeUsuarios.dtos.UserRolesPermissionsDTO;
import com.usuarios.servicioDeUsuarios.exceptions.NotFoundException;
import com.usuarios.servicioDeUsuarios.models.entities.Usuario;
import com.usuarios.servicioDeUsuarios.models.repositories.IUsuarioRepository;
import com.usuarios.servicioDeUsuarios.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LoginService {
    private final IUsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginService(IUsuarioRepository usuariosRepository) {
        this.usuarioRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getContrasenia())) {
            throw new NotFoundException("Usuario", username);
        }

        return usuario;
    }

    public String generarAccessToken(String username, String rol) {
        return JwtUtil.generarAccessToken(username, rol);
    }

    public String generarRefreshToken(String username, String rol) {
        return JwtUtil.generarRefreshToken(username, rol);
    }

    public UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        Usuario usuario = usuarioOpt.get();

        return UserRolesPermissionsDTO.builder()
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                //.permisos(usuario.getPermisos())
                .build();
    }


   /* private void validarDuplicidadDeUsuario(UsuarioDTO dto) {
        Optional<Usuario> alumnoExistente = usuarioRepository.findById(dto.getLegajo().trim());
        if(alumnoExistente.isPresent()) {
            throw new RuntimeException("Usuario Duplicado");
        }
    }*/
}
