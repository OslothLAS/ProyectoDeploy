package com.frontMetaMapa.frontMetaMapa.services;


import com.frontMetaMapa.frontMetaMapa.exceptions.DuplicateColeccionException;
import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.exceptions.ValidationException;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.input.ColeccionInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.ColeccionOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColeccionService {
    @Autowired
    private ColeccionesApiService coleccionApiService;

    public List<ColeccionOutputDTO> obtenerTodasLasColecciones() {
        return coleccionApiService.obtenerTodasLasColecciones();
    }

    public Optional<ColeccionOutputDTO> obtenerColeccionPorId(String id) {
        try {
            ColeccionOutputDTO coleccion = coleccionApiService.obtenerColeccionPorId(id);
            return Optional.of(coleccion);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionDTO) {
        validarDatosBasicos(coleccionDTO);
        validarDuplicidadDeColeccion(coleccionDTO);
        return coleccionApiService.crearColeccion(coleccionDTO);
    }

    public ColeccionOutputDTO actualizarColeccion(String id, ColeccionInputDTO coleccionDTO) {
        // Verificar que existe
        coleccionApiService.obtenerColeccionPorId(id);

        validarDatosBasicos(coleccionDTO);

        // Si cambia el id, validar duplicidad
        if (!id.equals(coleccionDTO.getId().trim())) {
            validarDuplicidadDeColeccion(coleccionDTO);
        }

        return coleccionApiService.actualizarColeccion(id, coleccionDTO);
    }


    public void eliminarColeccion(String id) {
        coleccionApiService.obtenerColeccionPorId(id); // Verificar que existe
        coleccionApiService.eliminarColeccion(id);
    }
    private void validarDatosBasicos(ColeccionInputDTO coleccionDTO) {
        ValidationException validationException = new ValidationException("Errores de validación");
        boolean tieneErrores = false;

        if (coleccionDTO.getId() == null || coleccionDTO.getId().trim().isEmpty()) {
            validationException.addFieldError("id", "El id es obligatorio");
            tieneErrores = true;
        }

        if (coleccionDTO.getTitulo() == null || coleccionDTO.getTitulo().trim().isEmpty()) {
            validationException.addFieldError("titulo", "El título es obligatorio");
            tieneErrores = true;
        }

        if (coleccionDTO.getDescripcion() == null || coleccionDTO.getDescripcion().trim().isEmpty()) {
            validationException.addFieldError("descripcion", "La descripción es obligatoria");
            tieneErrores = true;
        }

        if (coleccionDTO.getFuentes() == null || coleccionDTO.getFuentes().isEmpty()) {
            validationException.addFieldError("fuentes", "Debe tener al menos una fuente");
            tieneErrores = true;
        }

        if (coleccionDTO.getCriterios() == null || coleccionDTO.getCriterios().isEmpty()) {
            validationException.addFieldError("criterios", "Debe especificar criterios de pertenencia");
            tieneErrores = true;
        }

        if (coleccionDTO.getEstrategiaConsenso() == null || coleccionDTO.getEstrategiaConsenso().trim().isEmpty()) {
            validationException.addFieldError("estrategiaConsenso", "La estrategia de consenso es obligatoria");
            tieneErrores = true;
        }

        if (tieneErrores) {
            throw validationException;
        }
    }


    private void validarDuplicidadDeColeccion(ColeccionInputDTO coleccionDTO) {
        String id = coleccionDTO.getId().trim();

        if (coleccionApiService.existeColeccion(id)) {
            throw new DuplicateColeccionException(id);
        }
    }

}
