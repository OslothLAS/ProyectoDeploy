package ar.utn.ba.ddsi.fuenteDinamica.utils;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.*;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HechoUtil {
    public static HechoDTO hechoToDTO(Hecho hecho) {
        HechoDTO dto = new HechoDTO();

        List<Multimedia> multimediaNueva = new  ArrayList<>();

        if(hecho.getMultimedia() != null) {
            multimediaNueva.addAll(hecho.getMultimedia());
        }

        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria().getCategoria());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setMultimedia(multimediaNueva);

        if (hecho.getUbicacion() != null) {
            dto.setUbicacion(ubicacionToDTO(hecho.getUbicacion()));
        }

        dto.setMostrarDatos(hecho.getMostrarDatos());
        dto.setOrigen(hecho.getOrigen());
        dto.setMostrarDatos(hecho.getMostrarDatos());

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
        List<Multimedia> multimediaNueva = new  ArrayList<>();

        if(dto.getMultimedia() != null) {
            multimediaNueva.addAll(dto.getMultimedia());
        }

        Ubicacion ubi = getUbicacion(dto);
        Categoria cat = new Categoria(dto.getCategoria());
        
        return new Hecho(dto.getTitulo(), dto.getDescripcion(),cat,ubi,dto.getFechaHecho(),multimediaNueva
                ,dto.getOrigen(),dto.getMostrarDatos(),null);
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
