package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
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

    public static ColeccionOutputDTO coleccionToDto(Coleccion coleccion) {
        if (coleccion == null) {
            return null;
        }

        ColeccionOutputDTO dto = new ColeccionOutputDTO();
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setImportadores(coleccion.getImportadores());  // Direct reference (ensure proper JPA mappings)
        dto.setCriteriosDePertenencia(coleccion.getCriteriosDePertenencia());

        return dto;
    }



}
