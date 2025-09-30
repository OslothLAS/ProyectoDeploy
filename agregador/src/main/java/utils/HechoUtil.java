package utils;

import ar.utn.frba.ddsi.agregador.dtos.input.*;
import ar.utn.frba.ddsi.agregador.dtos.output.*;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.*;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HechoUtil {
    public static HechoOutputDTO hechoToDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        if (hecho.getAutor() != null) {
            AutorDTO autorDTO = new AutorDTO(hecho.getAutor().getNombre(),hecho.getAutor().getApellido(),hecho.getAutor().getFechaNacimiento(),hecho.getAutor().getTipo());
            dto.setAutor(autorDTO);
        }

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
        dto.setEtiquetas(hecho.getEtiquetas());
        dto.setColecciones(hecho.getColecciones());
        dto.setHandles(hecho.getHandles());
        dto.setOrigen(hecho.getOrigen());
        dto.setFuenteOrigen(hecho.getFuenteOrigen());
        dto.setMostrarDatos(hecho.getMostrarDatos());
        dto.setFechaCreacion(hecho.getFechaCreacion());
        dto.setPlazoEdicion(hecho.getPlazoEdicion());
        dto.setEsEditable(hecho.getEsEditable());

        dto.setTitulo(hecho.getDatosHechos().getTitulo());
        dto.setDescripcion(hecho.getDatosHechos().getDescripcion());
        dto.setFechaHecho(hecho.getDatosHechos().getFechaHecho());

        if (hecho.getDatosHechos().getCategoria() != null) {
            CategoriaDTO cat = new CategoriaDTO(hecho.getDatosHechos().getCategoria().getCategoria());
            dto.setCategoria(cat);
        }

        if (hecho.getDatosHechos().getUbicacion() != null) {
            dto.setUbicacion(ubicacionToDTO(hecho.getDatosHechos().getUbicacion()));
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

    public static List<HechoOutputDTO> hechosToDTO(List<Hecho> hechos) {
        return hechos.stream()
                .map(HechoUtil::hechoToDTO)
                .collect(Collectors.toList());
    }

    public static Hecho hechoDTOtoHecho(HechoOutputDTO dto){
        DatosHechos datos = getDatosHechos(dto);

        Usuario autor = null;

        if(dto.getAutor() != null) {
            autor = new Usuario(dto.getAutor().getNombre(), dto.getAutor().getApellido(), dto.getAutor().getFechaNacimiento(), dto.getAutor().getTipo());
        }

        List<Multimedia> multimediaNueva = null;
        if(dto.getMultimedia() != null){
            multimediaNueva = new ArrayList<>();
            for (MultimediaDTO m : dto.getMultimedia()) {
                Multimedia nuevo = new Multimedia();
                nuevo.setUrl(m.getUrl());
                multimediaNueva.add(nuevo);
            }
        }


        return new Hecho(null, autor, dto.getEsValido(), datos, multimediaNueva, dto.getEtiquetas(),
                dto.getColecciones(), dto.getHandles(), dto.getOrigen(), dto.getFuenteOrigen(), dto.getMostrarDatos(),
                dto.getFechaCreacion(), dto.getPlazoEdicion(), dto.getEsEditable(),null
        );
    }

    private static DatosHechos getDatosHechos(HechoOutputDTO dto) {
        Provincia provincia = new Provincia(null);
        Localidad localidad = new Localidad(provincia,null);

        if (dto.getUbicacion().getLocalidad() != null) {
            if (dto.getUbicacion().getLocalidad().getProvincia() != null) {
                provincia.setNombre(dto.getUbicacion().getLocalidad().getProvincia().getNombre());
            }
            localidad.setNombre(dto.getUbicacion().getLocalidad().getNombre());
            localidad.setProvincia(provincia);
        }
        Ubicacion ubi = new Ubicacion(dto.getUbicacion().getLatitud(), dto.getUbicacion().getLongitud(),localidad);
        Categoria cat = new Categoria(dto.getCategoria().getCategoria());

        return new DatosHechos(dto.getTitulo(), dto.getDescripcion(),cat,ubi, dto.getFechaHecho());
    }
}
