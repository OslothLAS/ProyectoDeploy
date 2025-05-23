package ar.utn.ba.ddsi.fuenteProxy.mappers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import entities.hechos.Hecho;

public class HechoMapper {

    public static Hecho map(HechoDto dto) {
        return Hecho.builder()
                .autor(null)
                .esValido(true)
                .etiquetas(null)
                .colecciones(null)
                .mostrarDatos(null)
                .build();
    }
}