package ar.utn.ba.ddsi.fuenteProxy.mappers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import entities.hechos.DatosHechos;
import entities.hechos.Ubicacion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class DatosHechosMapper {

    public static DatosHechos map(HechoDto dto) {
        return DatosHechos.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(dto.getCategoria())
                .ubicacion(new Ubicacion(
                        dto.getLatitud() != null ? dto.getLatitud().toString() : null,
                        dto.getLongitud() != null ? dto.getLongitud().toString() : null
                ))
                .fechaHecho(parseFecha(dto.getFecha_hecho()))
                .fechaCarga(parseFecha(dto.getCreated_at()))
                .origen(null) // completar si aplica
                .build();
    }

    private static LocalDate parseFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) return null;
        return LocalDate.parse(fecha, DateTimeFormatter.ISO_DATE); // o ajustá el formato si es distinto
    }
}
