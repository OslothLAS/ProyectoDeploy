package com.frontMetaMapa.frontMetaMapa.services;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
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
        //validarDatosBasicos(coleccionDTO);
        //validarContactos(coleccionDTO);
        validarDuplicidadDeColeccion(coleccionDTO);

        return coleccionApiService.crearColeccion(coleccionDTO);
    }

    public ColeccionOutputDTO actualizarColeccion(String id, ColeccionInputDTO coleccionDTO) {
        // Verificar que el alumno existe
        coleccionApiService.obtenerColeccionPorId(id);

        //validarDatosBasicos(coleccionDTO);
        //validarContactos(coleccionDTO);

        // Si el id cambió, verificar que no exista otro con el nuevo id
        if (!id.equals(coleccionDTO.getLegajo().trim())) {
            validarDuplicidadDeColeccion(coleccionDTO);
        }

        return coleccionApiService.actualizarColeccion(id, coleccionDTO);
    }

    public void eliminarColeccion(String id) {
        coleccionApiService.obtenerColeccionPorId(id); // Verificar que existe
        coleccionApiService.eliminarColeccion(id);
    }
//Hay que aca validar campo por campo
    /*
    private void validarDatosBasicos(ColeccionOutputDTO coleccionDTO) {
        ValidationException validationException = new ValidationException("Errores de validación");
        boolean tieneErrores = false;

        if (coleccionDTO.getLegajo() == null || coleccionDTO.getLegajo().trim().isEmpty()) {
            validationException.addFieldError("id", "El id es obligatorio");
            tieneErrores = true;
        }

        if (coleccionDTO.getNombre() == null || coleccionDTO.getNombre().trim().isEmpty()) {
            validationException.addFieldError("nombre", "El nombre es obligatorio");
            tieneErrores = true;
        }

        if (coleccionDTO.getApellido() == null || coleccionDTO.getApellido().trim().isEmpty()) {
            validationException.addFieldError("apellido", "El apellido es obligatorio");
            tieneErrores = true;
        }

        if (tieneErrores) {
            throw validationException;
        }
    }
*/

    private void validarDuplicidadDeColeccion(ColeccionOutputDTO coleccionDTO) {
        if (coleccionApiService.existeColeccion(coleccionDTO.getLegajo().trim())) {
            throw new DuplicateLegajoException(coleccionDTO.getLegajo().trim());
        }
    }
}
