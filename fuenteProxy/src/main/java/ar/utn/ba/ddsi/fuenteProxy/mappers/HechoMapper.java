package ar.utn.ba.ddsi.fuenteProxy.mappers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.hecho.HechoDto;
import entities.colecciones.Coleccion;
import entities.colecciones.Handle;
import entities.hechos.Categoria;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Ubicacion;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HechoMapper {

    public static Hecho mapHechoDtoToHecho(HechoDto dto) {
        List<Handle> handles = dto.getColecciones();

        DatosHechos datosHechos = DatosHechos.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(new Categoria(dto.getCategoria()))
                .ubicacion(new Ubicacion(
                        String.valueOf(dto.getLatitud()),
                        String.valueOf(dto.getLongitud()),
                        null
                ))
                .fechaHecho(parseDate(dto.getFechaHecho()))
                .build();
        return Hecho.create(datosHechos,null ,handles, dto.getEsConsensuado());
    }


    private static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;

        try {
            return java.time.OffsetDateTime.parse(dateString).toLocalDate();
        } catch (Exception e) {
            System.err.println("Error al parsear la fecha: " + dateString);
            return null;
        }
    }
}