package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import entities.colecciones.Coleccion;
import entities.colecciones.Fuente;

import java.util.List;

public class ColeccionUtil {

    public static Coleccion dtoToColeccion(ColeccionInputDTO dto, List<Fuente> importadores) {
        // Convertir los nombres de importadores a objetos Importador

        // Crear la nueva colección
        return new Coleccion(
                dto.getTitulo(),
                dto.getDescripcion(),
                importadores,
                dto.getCriterios()
        );
    }

    public static ColeccionInputDTO coleccionToDto(Coleccion coleccion) {
        ColeccionInputDTO dto = new ColeccionInputDTO();

        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        //dto.setImportadores(nombresImportadores);
        dto.setCriterios(coleccion.getCriteriosDePertenencia());

        //ver como setear los importadores
        return dto;
    }

}
