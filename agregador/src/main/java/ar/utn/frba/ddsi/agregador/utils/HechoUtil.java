package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.dtos.input.*;
import ar.utn.frba.ddsi.agregador.dtos.output.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Handle;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.*;

import java.util.*;
import java.util.stream.Collectors;

public class HechoUtil {


    public static List<Hecho> obtenerHechosValidos(List<Hecho> todosLosHechos) {
        return todosLosHechos.stream()
                .filter(hecho -> Boolean.TRUE.equals(hecho.getEsValido()))
                .collect(Collectors.toList());
    }

    public static List<Hecho> filtrarHechosRepetidos(List<Hecho> listaExistentes, List<Hecho> listaNuevos) {
        // Combinar ambas listas
        List<Hecho> todosLosHechos = new ArrayList<>();
        todosLosHechos.addAll(listaExistentes);
        todosLosHechos.addAll(listaNuevos);

        // Mapa para agrupar hechos por su clave única (titulo + descripcion)
        Map<String, List<Hecho>> hechosAgrupados = new HashMap<>();

        // Agrupar hechos con mismo título y descripción
        for (Hecho hecho : todosLosHechos) {
            String clave = generarClave(hecho.getTitulo(), hecho.getDescripcion());
            hechosAgrupados.computeIfAbsent(clave, k -> new ArrayList<>()).add(hecho);
        }

        // Procesar cada grupo de hechos repetidos
        for (List<Hecho> grupoRepetidos : hechosAgrupados.values()) {
            if (grupoRepetidos.size() > 1) {
                // Marcar todos como inválidos primero
                grupoRepetidos.forEach(h -> h.setEsValido(false));

                // Seleccionar uno para mantener como válido (el más antiguo o el primero)
                Hecho hechoAMantener = seleccionarHechoValido(grupoRepetidos);
                hechoAMantener.setEsValido(true);
            } else {
                // Si no hay repetidos, mantener el estado actual o marcarlo como válido
                Hecho hecho = grupoRepetidos.get(0);
                if (hecho.getEsValido() == null) {
                    hecho.setEsValido(true);
                }
            }
        }

        return todosLosHechos;
    }

    /**
     * Genera una clave única combinando título y descripción
     * Normaliza los strings para evitar problemas con espacios y mayúsculas
     */
    private static String generarClave(String titulo, String descripcion) {
        String tituloNorm = (titulo != null) ? titulo.trim().toLowerCase() : "";
        String descNorm = (descripcion != null) ? descripcion.trim().toLowerCase() : "";
        return tituloNorm + "|" + descNorm;
    }

    private static Hecho seleccionarHechoValido(List<Hecho> hechos) {
        // Priorizar hechos que ya existen en BD (tienen ID)
        Hecho hechoConId = hechos.stream()
                .filter(h -> h.getId() != null)
                .findFirst()
                .orElse(null);

        if (hechoConId != null) {
            return hechoConId;
        }

        return hechos.stream()
                .filter(h -> h.getFechaCreacion() != null)
                .min((h1, h2) -> h1.getFechaCreacion().compareTo(h2.getFechaCreacion()))
                .orElse(hechos.get(0)); // Si no hay fechas, devolver el primero
    }


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
