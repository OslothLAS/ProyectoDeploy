package ar.utn.ba.ddsi.fuenteDinamica.utils;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.MultimediaDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HechoUtil {
    public static HechoDTO hechoToDTO(Hecho hecho) {
        HechoDTO dto = new HechoDTO();

        List<MultimediaDTO> multimediaNueva = null;

        if(hecho.getMultimedia() != null) {
            multimediaNueva = new ArrayList<>();
            for (Multimedia m : hecho.getMultimedia()) {
                MultimediaDTO nuevo = new MultimediaDTO();
                nuevo.setUrl(m.getUrl());
                multimediaNueva.add(nuevo);
            }
        }

        dto.setEsValido(hecho.getEsValido());
        dto.setMultimedia(multimediaNueva);
        dto.setOrigen(hecho.getOrigen());
        dto.setFuenteOrigen(hecho.getFuenteOrigen());
        dto.setFechaCreacion(hecho.getFechaCreacion());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setCategoria(hecho.getCategoria().getCategoria());

        if (hecho.getUbicacion() != null) {
            dto.setUbicacion(ubicacionToDTO(hecho.getUbicacion()));
        }

        return dto;
    }

    private static UbicacionDTO ubicacionToDTO(Ubicacion ubicacion) {
        if (ubicacion == null) {
            return null;
        }

        UbicacionDTO ubicacionDTO = new UbicacionDTO();
        ubicacionDTO.setLatitud(ubicacion.getLatitud());
        ubicacionDTO.setLongitud(ubicacion.getLongitud());

        if (ubicacion.getLocalidad() != null) {
            LocalidadDTO localidadDTO = new LocalidadDTO();
            localidadDTO.setNombre(ubicacion.getLocalidad().getNombre());

            if (ubicacion.getLocalidad().getProvincia() != null) {
                ProvinciaDTO provinciaDTO = new ProvinciaDTO();
                provinciaDTO.setNombre(ubicacion.getLocalidad().getProvincia().getNombre());
                localidadDTO.setProvincia(provinciaDTO);
            }

            ubicacionDTO.setLocalidad(localidadDTO);
        }

        return ubicacionDTO;
    }

    public static List<HechoDTO> hechosToDTO(List<Hecho> hechos) {
        return hechos.stream()
                .map(HechoUtil::hechoToDTO)
                .collect(Collectors.toList());
    }

    public static Hecho hechoDTOtoHecho(HechoDTO dto){
        List<Multimedia> multimediaNueva = null;
        if(dto.getMultimedia() != null){
            multimediaNueva = new ArrayList<>();
            for (MultimediaDTO m : dto.getMultimedia()) {
                Multimedia nuevo = new Multimedia();
                nuevo.setUrl(m.getUrl());
                multimediaNueva.add(nuevo);
            }
        }

        Ubicacion ubi = getUbicacion(dto);
        Categoria cat = new Categoria(dto.getCategoria());
        
        return new Hecho(dto.getTitulo(), dto.getDescripcion(),cat,ubi,dto.getFechaHecho(),multimediaNueva
                ,dto.getOrigen(),dto.getFuenteOrigen(),dto.getFechaCreacion(),dto.getEsValido());
    }


    private static Ubicacion getUbicacion(HechoDTO dto) {
        Provincia provincia = new Provincia(null);
        Localidad localidad = new Localidad(provincia,null);

        if (dto.getUbicacion().getLocalidad() != null) {
            if (dto.getUbicacion().getLocalidad().getProvincia() != null) {
                provincia.setNombre(dto.getUbicacion().getLocalidad().getProvincia().getNombre());
            }
            localidad.setNombre(dto.getUbicacion().getLocalidad().getNombre());
            localidad.setProvincia(provincia);
        }
        return new Ubicacion(dto.getUbicacion().getLatitud(), dto.getUbicacion().getLongitud(),localidad);
    }
}
