package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.exceptions.ValidationException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.*;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public void crearHecho(HechoInputDTO dto) {
        validarDatosBasicos(dto);

        UbicacionDTO ubicacion = new UbicacionDTO();
        ubicacion.setLatitud(dto.getLatitud());
        ubicacion.setLongitud(dto.getLongitud());

        LocalidadDTO localidad = new LocalidadDTO();
        localidad.setNombre(dto.getLocalidad());

        ProvinciaDTO provincia = new ProvinciaDTO();
        provincia.setId(dto.getProvincia());

        localidad.setProvincia(provincia);
        ubicacion.setLocalidad(localidad);

        // 🔹 Crear el DTO que se enviará al backend
        HechoApiDto apiDTO = new HechoApiDto();
        apiDTO.setTitulo(dto.getTitulo());
        apiDTO.setDescripcion(dto.getDescripcion());
        apiDTO.setCategoria(dto.getCategoria());
        apiDTO.setUbicacion(ubicacion);
        apiDTO.setFechaHecho(dto.getFechaHecho());
        apiDTO.setMostrarDatos(dto.getMostrarDatos());

        // 🔹 Manejo opcional de archivos multimedia (si los subís)
        if (dto.getMultimedia() != null && !dto.getMultimedia().isEmpty()) {
            List<MultimediaDTO> multimediaList = new ArrayList<>();

            for (MultipartFile file : dto.getMultimedia()) {
                // Por ahora solo pasás la URL si ya existe,
                // o lo que necesite tu backend (ej. subir a un storage primero)
                MultimediaDTO media = new MultimediaDTO();
                media.setUrl(file.getOriginalFilename()); // o el path al subirlo
                multimediaList.add(media);
            }

            apiDTO.setMultimedia(multimediaList);
        }

        // 🔹 Llamar al servicio que hace el POST real
        hechoApiService.createHecho(apiDTO);
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
