package ar.utn.ba.ddsi.fuenteProxy.mappers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import entities.hechos.Ubicacion;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HechoMapper {

    public static Hecho mapHechoDtoToHecho(HechoDto dto) {
        DatosHechos datosHechos = DatosHechos.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(dto.getCategoria())
                .ubicacion(new Ubicacion(
                        String.valueOf(dto.getLatitud()),
                        String.valueOf(dto.getLongitud())
                ))
                .fechaHecho(parseDate(dto.getFecha_hecho()))
                .build();
        return Hecho.create(datosHechos);
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