package com.usuarios.servicioDeUsuarios.services;

import com.usuarios.servicioDeUsuarios.dtos.UsuarioDTO;
import com.usuarios.servicioDeUsuarios.exceptions.ValidationException;
import com.usuarios.servicioDeUsuarios.models.entities.Usuario;
import com.usuarios.servicioDeUsuarios.models.repositories.IUsuarioRepository;
import com.usuarios.servicioDeUsuarios.utils.UsuarioUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {
    private final IUsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(IUsuarioRepository usuariosRepository) {
        this.usuarioRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username)) ;
        GrantedAuthority authoritie = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());
        return User.withUsername(usuario.getUsername())
                .password(usuario.getContrasenia())
                .authorities(authoritie)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioUtil::usuarioToDTO)
                .toList();
    }

    public UsuarioDTO findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(UsuarioUtil::usuarioToDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Long crearUsuario(UsuarioDTO usuarioDTO) {
        validarDatosBasicos(usuarioDTO);
        Usuario usuario = UsuarioUtil.usuarioDTOToEntity(usuarioDTO);

        String contraseniaEncriptada = passwordEncoder.encode(usuario.getContrasenia());
        usuario.setContrasenia(contraseniaEncriptada);

        usuarioRepository.save(usuario);

        return usuario.getId();
    }

    private void validarDatosBasicos(UsuarioDTO dto) {
        ValidationException validationException = new ValidationException("Errores de validación");
        boolean tieneErrores = false;

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            validationException.addFieldError("nombre", "El nombre es obligatorio");
            tieneErrores = true;
        }

        if (dto.getApellido() == null || dto.getApellido().trim().isEmpty()) {
            validationException.addFieldError("apellido", "El apellido es obligatorio");
            tieneErrores = true;
        }

        if (tieneErrores) {
            throw validationException;
        }
    }


}
