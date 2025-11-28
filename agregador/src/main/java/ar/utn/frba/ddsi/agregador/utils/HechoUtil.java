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

    public static List<Hecho> filtrarHechosRepetidos(List<Hecho> existentes, List<Hecho> nuevos) {

        // Mapa para guardar hechos existentes por su clave
        Map<String, Hecho> existentesPorClave = new HashMap<>();

        // Primero guardamos todos los hechos existentes en el mapa
        for (Hecho h : existentes) {
            String claveFuente = generarClave(h.getTitulo(), h.getDescripcion()) + "_" + h.getFuenteOrigen();
            existentesPorClave.put(claveFuente, h);
        }

        List<Hecho> result = new ArrayList<>();
        Set<String> clavesYaProcesadas = new HashSet<>();

        for (Hecho h : nuevos) {
            String claveFuente = generarClave(h.getTitulo(), h.getDescripcion()) + "_" + h.getFuenteOrigen();

            // Si ya procesamos esta clave, saltar
            if (clavesYaProcesadas.contains(claveFuente)) {
                continue;
            }

            // Si existe en la BD, agregar el existente
            if (existentesPorClave.containsKey(claveFuente)) {
                result.add(existentesPorClave.get(claveFuente));
            } else {
                // Si no existe, agregar el nuevo
                result.add(h);
            }

            clavesYaProcesadas.add(claveFuente);
        }

        return result;
    }

    /**
     * Genera una clave única combinando título y descripción
     * Normaliza los strings para evitar problemas con espacios y mayúsculas
     */
    public static String generarClave(String titulo, String descripcion) {
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

        Long idDinamica = null;
        if(dto.getFuenteOrigen() == FuenteOrigen.DINAMICO){
            idDinamica = dto.getId();
            System.out.println("\n" + "el id de dinamica es" +  idDinamica + "\n" + "." + "\n");
        }

        return new Hecho(null, dto.getUsername(), dto.getEsValido(), dto.getTitulo(),dto.getDescripcion(),new Categoria(dto.getCategoria()),
                ubi,dto.getFechaHecho(),multimediaNueva, dto.getEtiquetas(),null,
                handles, dto.getOrigen(), dto.getFuenteOrigen(), dto.getMostrarDatos(),
                dto.getFechaCreacion(), dto.getPlazoEdicion(), dto.getEsEditable(),false,idDinamica);
    }

}
