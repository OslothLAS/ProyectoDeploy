package ar.utn.frba.ddsi.agregador.utils;
import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.CriterioDePertenenciaDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.FuenteDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioPorCategoria;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioPorFecha;
import ar.utn.frba.ddsi.agregador.models.entities.factories.ConsensoFactory;
import java.util.List;
import java.util.stream.Collectors;

public class ColeccionUtil {

    public static Coleccion dtoToColeccion(ColeccionInputDTO dto, List<Fuente> importadores) {
        return new Coleccion(
                dto.getTitulo(),
                dto.getDescripcion(),
                importadores,
                dto.getCriterios(),
                ConsensoFactory.getStrategy(dto.getEstrategiaConsenso())
        );
    }
    public static ColeccionOutputDTO coleccionToDto(Coleccion coleccion) {
        if (coleccion == null) {
            return null;
        }

        ColeccionOutputDTO dto = new ColeccionOutputDTO();
        dto.setId(coleccion.getId());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());

        // Importadores
        dto.setImportadores(
                coleccion.getImportadores().stream()
                        .map(ColeccionUtil::fuenteToFuenteDTO)
                        .collect(Collectors.toList())
        );

        // Criterios
        dto.setCriteriosDePertenencia(
                coleccion.getCriteriosDePertenencia().stream()
                        .map(ColeccionUtil::criterioToDTO)
                        .collect(Collectors.toList())
        );

        dto.setHandle(coleccion.getHandle().getValue());
        dto.setFechaYHoraDeActualizacion(coleccion.getFechaYHoraDeActualizacion());
        dto.setConsenso(coleccion.getConsenso().getNombre());

        return dto;
    }




    public static Fuente fuenteDTOtoFuente(FuenteInputDTO fuenteDTO){
        return new Fuente(fuenteDTO.getId(), fuenteDTO.getUrl());
    }

    public static FuenteDTO fuenteToFuenteDTO(Fuente fuente){
        return new FuenteDTO(fuente.getId(), fuente.getUrl(),fuente.getOrigenHechos());
    }

    public static CriterioDePertenenciaDTO criterioToDTO(CriterioDePertenencia criterio) {
        if (criterio instanceof CriterioPorCategoria cat) {
            return new CriterioDePertenenciaDTO(
                    cat.getId(),
                    "categoria",
                    cat.getCategoria() != null ? cat.getCategoria().getCategoria() : null
            );
        } else if (criterio instanceof CriterioPorFecha fecha) {
            String valor = fecha.getFechaInicio() + " - " + fecha.getFechaFin();
            return new CriterioDePertenenciaDTO(
                    fecha.getId(),
                    "fecha",
                    valor
            );
        } else {
            return new CriterioDePertenenciaDTO(
                    criterio.getId(),
                    "desconocido",
                    null
            );
        }
    }

}
