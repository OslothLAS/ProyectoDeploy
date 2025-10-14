package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.exceptions.ValidationException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HechoService {

    @Autowired
    private HechosApiService hechoApiService;

    // Obtener todos los hechos
    public List<HechoOutputDTO> obtenerTodosLosHechos() {
        return hechoApiService.obtenerHechos();
    }

    // Obtener hecho por ID
    public Optional<HechoOutputDTO> obtenerHechoPorId(Long id) {
        try {
            HechoOutputDTO hecho = hechoApiService.actualizarHecho(id, null); // Solo para verificar existencia
            return Optional.of(hecho);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    // Crear un hecho
    public HechoOutputDTO crearHecho(HechoInputDTO hechoDTO) {
        validarDatosBasicos(hechoDTO);
        return hechoApiService.createHecho(hechoDTO);
    }

    // Actualizar un hecho
    public HechoOutputDTO actualizarHecho(Long id, HechoInputDTO hechoDTO) {
        // Verificar que existe
        hechoApiService.obtenerHechos().stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Hecho", id.toString()));

        validarDatosBasicos(hechoDTO);

        return hechoApiService.actualizarHecho(id, hechoDTO);
    }

    // Validaciones básicas
    private void validarDatosBasicos(HechoInputDTO hechoDTO) {
        ValidationException validationException = new ValidationException("Errores de validación");
        boolean tieneErrores = false;

        if (hechoDTO.getTitulo() == null || hechoDTO.getTitulo().trim().isEmpty()) {
            validationException.addFieldError("titulo", "El título es obligatorio");
            tieneErrores = true;
        }

        if (hechoDTO.getDescripcion() == null || hechoDTO.getDescripcion().trim().isEmpty()) {
            validationException.addFieldError("descripcion", "La descripción es obligatoria");
            tieneErrores = true;
        }

        if (hechoDTO.getCategoria() == null) {
            validationException.addFieldError("categoria", "La categoría es obligatoria");
            tieneErrores = true;
        }

        if (hechoDTO.getFechaHecho() == null) {
            validationException.addFieldError("fechaHecho", "La fecha del hecho es obligatoria");
            tieneErrores = true;
        }

        if (tieneErrores) {
            throw validationException;
        }
    }
}
