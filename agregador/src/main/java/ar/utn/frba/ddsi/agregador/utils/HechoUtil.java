package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.dtos.input.*;
import ar.utn.frba.ddsi.agregador.dtos.output.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Handle;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HechoUtil {

    public static HechoOutputDTO hechoToDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();


        List<MultimediaDTO> multimediaNueva = null;
        if(hecho.getMultimedia() != null) {
            multimediaNueva = new ArrayList<>();
            for (Multimedia m : hecho.getMultimedia()) {
                MultimediaDTO nuevo = new MultimediaDTO();
                nuevo.setUrl(m.getUrl());
                multimediaNueva.add(nuevo);
            }
        }

        List<String> handles = null;
        if(dto.getHandles() != null){
            handles = new ArrayList<>();
            for (Handle h : hecho.getHandles()) {
                String nuevo = h.getValue();
                handles.add(nuevo);
            }
        }

        dto.setId(hecho.getId());
        dto.setUsername(hecho.getUsername());
        dto.setEsValido(hecho.getEsValido());
        dto.setMultimedia(multimediaNueva);
        dto.setEtiquetas(hecho.getEtiquetas());
        //dto.setColecciones(hecho.getColecciones());
        dto.setHandles(handles);
        dto.setOrigen(hecho.getOrigen());
        dto.setFuenteOrigen(hecho.getFuenteOrigen());
        dto.setMostrarDatos(hecho.getMostrarDatos());
        dto.setFechaCreacion(hecho.getFechaCreacion());
        dto.setPlazoEdicion(hecho.getPlazoEdicion());
        dto.setEsEditable(hecho.getEsEditable());

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
                provinciaDTO.setId(ubicacion.getLocalidad().getProvincia().getId());
                localidadDTO.setProvincia(provinciaDTO);
            }

            ubicacionDTO.setLocalidad(localidadDTO);
        }

        return ubicacionDTO;
    }

    public static List<HechoOutputDTO> hechosToDTO(List<Hecho> hechos) {
        return hechos.stream()
                .map(HechoUtil::hechoToDTO)
                .collect(Collectors.toList());
    }

    public static Hecho hechoDTOtoHecho(HechoOutputDTO dto){
        List<Multimedia> multimediaNueva = null;
        if(dto.getMultimedia() != null){
            multimediaNueva = new ArrayList<>();
            for (MultimediaDTO m : dto.getMultimedia()) {
                Multimedia nuevo = new Multimedia();
                nuevo.setUrl(m.getUrl());
                multimediaNueva.add(nuevo);
            }
        }

        List<Handle> handles = null;
        if(dto.getHandles() != null){
            handles = new ArrayList<>();
            for (String m : dto.getHandles()) {
                Handle nuevo = new Handle(m);
                handles.add(nuevo);
            }
        }

        Provincia provincia = new Provincia(null);
        Localidad localidad = new Localidad(provincia,null);

        if (dto.getUbicacion().getLocalidad() != null) {
            if (dto.getUbicacion().getLocalidad().getProvincia() != null) {
                provincia.setId(dto.getUbicacion().getLocalidad().getProvincia().getId());
            }
            localidad.setNombre(dto.getUbicacion().getLocalidad().getNombre());
            localidad.setProvincia(provincia);
        }
        Ubicacion ubi = new Ubicacion(dto.getUbicacion().getLatitud(), dto.getUbicacion().getLongitud(),localidad);

        return new Hecho(null, dto.getUsername(), dto.getEsValido(), dto.getTitulo(),dto.getDescripcion(),new Categoria(dto.getCategoria()),
                ubi,dto.getFechaHecho(),multimediaNueva, dto.getEtiquetas(),null,
                handles, dto.getOrigen(), dto.getFuenteOrigen(), dto.getMostrarDatos(),
                dto.getFechaCreacion(), dto.getPlazoEdicion(), dto.getEsEditable(),null
        );
    }

}
