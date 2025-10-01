package com.usuarios.servicioDeUsuarios.services;

import com.usuarios.servicioDeUsuarios.dtos.UsuarioDTO;
import com.usuarios.servicioDeUsuarios.exceptions.ValidationException;
import com.usuarios.servicioDeUsuarios.models.repositories.IUsuarioRepository;
import com.usuarios.servicioDeUsuarios.utils.UsuarioUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuariosRepository) {
        this.usuarioRepository = usuariosRepository;
    }

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioUtil::usuarioToDTO)
                .toList();
    }

    public Optional<UsuarioDTO> findByUsername(String username) {
        return usuarioRepository.findByUsername(username).map(UsuarioUtil::usuarioToDTO);
    }

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        validarDatosBasicos(usuarioDTO);
        //TODO
        return usuarioDTO;
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
