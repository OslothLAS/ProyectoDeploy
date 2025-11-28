package com.frontMetaMapa.frontMetaMapa.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.exceptions.ValidationException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoInputEditarApi;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.*;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HechoService {
    @Autowired private ObjectMapper objectMapper;
    @Autowired
    private HechosApiService hechoApiService;

    // Obtener todos los hechos
    public List<HechoApiOutputDto> obtenerTodosLosHechos() {
        return hechoApiService.obtenerHechos();
    }

    public  List<HechoApiOutputDto> obtenerHechosPorUsername(String username) {
        return hechoApiService.getHechosByUsername(username);
    }

    // Obtener hecho por ID
    public Optional<HechoApiOutputDto> obtenerHechoPorId(Long id) {
        try {
            HechoApiOutputDto hecho = hechoApiService.obtenerHechoPorId(id);
            return Optional.of(hecho);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    public void crearHechoMultipart(HechoInputDTO formDto, List<MultipartFile> archivos) throws JsonProcessingException {
        hechoApiService.crearHechoMultipart(formDto, archivos);
    }



    public Optional<HechoApiOutputDto> obtenerHechoDinamicoPorId(Long id) {
        try {
            HechoApiOutputDto hecho = hechoApiService.obtenerHechoDinamicoPorId(id);
            return Optional.of(hecho);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    // Obtener hecho por ID
    public Optional<HechoApiOutputDto> obtenerHechoPorIdPorColeccion(Long id) {
        try {
            HechoApiOutputDto hecho = hechoApiService.obtenerHechoPorIdPorColeccion(id);
            return Optional.of(hecho);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    // Crear un hecho
    public void crearHecho(HechoInputDTO dto, String username) {
        validarDatosBasicos(dto);

        HechoApiInputDto apiDTO = mapearAHechoApiDto(dto);
        apiDTO.setUsername(username);
        // 🔹 Llamar al servicio que hace el POST real
        hechoApiService.createHecho(apiDTO);
    }

    // Actualizar un hecho
    public void actualizarHecho(Long id, HechoInputEditarApi hechoDTO) {

        hechoApiService.actualizarHecho(id, hechoDTO);
    }

    public void importarHechosCSV(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("El archivo CSV está vacío.");
        }

        // Delega la llamada HTTP real al ApiService
        hechoApiService.importarHechosCSV(file);
    }

    // 🧩 Mapeo de DTO de entrada a DTO del backend (API)
    private HechoApiInputDto mapearAHechoApiDto(HechoInputDTO dto) {
        // Ubicación
        UbicacionDTO ubicacion = new UbicacionDTO();
        ubicacion.setLatitud(dto.getLatitud());
        ubicacion.setLongitud(dto.getLongitud());

        LocalidadDTO localidad = new LocalidadDTO();
        localidad.setNombre(dto.getLocalidad());

        ProvinciaDTO provincia = new ProvinciaDTO();
        provincia.setId(dto.getProvincia());

        localidad.setProvincia(provincia);
        ubicacion.setLocalidad(localidad);

        // Crear DTO API
        HechoApiInputDto apiDTO = new HechoApiInputDto();
        apiDTO.setTitulo(dto.getTitulo());
        apiDTO.setDescripcion(dto.getDescripcion());
        apiDTO.setCategoria(dto.getCategoria());
        apiDTO.setUbicacion(ubicacion);
        apiDTO.setFechaHecho(dto.getFechaHecho());
        apiDTO.setMostrarDatos(dto.getMostrarDatos());

        // Multimedia
        if (dto.getMultimedia() != null && !dto.getMultimedia().isEmpty()) {
            List<MultimediaDTO> multimediaList = new ArrayList<>();
            for (MultipartFile file : dto.getMultimedia()) {
                MultimediaDTO media = new MultimediaDTO();
                media.setUrl(file.getOriginalFilename()); // o el path al subirlo
                multimediaList.add(media);
            }
            apiDTO.setMultimedia(multimediaList);
        }

        return apiDTO;
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
