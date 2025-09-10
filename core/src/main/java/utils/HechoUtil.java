package utils;

import entities.dtos.*;
import entities.hechos.*;
import entities.usuarios.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public class HechoUtil {
    public static HechoOutputDTO hechoToDTO(Hecho hecho) {
        AutorDTO autorDTO = new AutorDTO(hecho.getAutor().getNombre(),hecho.getAutor().getApellido(),hecho.getAutor().getFechaNacimiento(),hecho.getAutor().getTipo());
        HechoOutputDTO dto = new HechoOutputDTO();

        if (hecho.getAutor() != null) {
            dto.setAutor(autorDTO);
        }

        dto.setEsValido(hecho.getEsValido());
        dto.setMultimedia(hecho.getMultimedia());
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

        Usuario autor = new Usuario(dto.getAutor().getNombre(),dto.getAutor().getApellido(),dto.getAutor().getFechaNacimiento(),dto.getAutor().getTipo());

        return new Hecho(null, autor, dto.getEsValido(), datos, dto.getMultimedia(), dto.getEtiquetas(),
                dto.getColecciones(), dto.getHandles(), dto.getOrigen(), dto.getFuenteOrigen(), dto.getMostrarDatos(),
                dto.getFechaCreacion(), dto.getPlazoEdicion(), dto.getEsEditable(),null
        );
    }

    private static DatosHechos getDatosHechos(HechoOutputDTO dto) {
        Provincia provincia = new Provincia(dto.getUbicacion().getLocalidad().getProvincia().getNombre());
        Localidad localidad = new Localidad(provincia, dto.getUbicacion().getLocalidad().getNombre());
        Ubicacion ubi = new Ubicacion(dto.getUbicacion().getLatitud(), dto.getUbicacion().getLongitud(),localidad);

        Categoria cat = new Categoria(dto.getCategoria().getCategoria());

        return new DatosHechos(dto.getTitulo(), dto.getDescripcion(),cat,ubi, dto.getFechaHecho());
    }
}
