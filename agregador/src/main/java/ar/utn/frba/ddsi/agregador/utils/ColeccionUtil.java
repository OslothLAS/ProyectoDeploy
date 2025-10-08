package ar.utn.frba.ddsi.agregador.utils;
import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.FuenteDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.factories.ConsensoFactory;
import java.util.List;
import java.util.stream.Collectors;

public class ColeccionUtil {

    public static Coleccion dtoToColeccion(ColeccionInputDTO dto, List<Fuente> importadores) {
        // Convertir los nombres de importadores a objetos Importador

        // Crear la nueva colección
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
        dto.setImportadores( coleccion.getImportadores()
                .stream()
                .map(ColeccionUtil::fuenteToFuenteDTO)
                .collect(Collectors.toList()));
        dto.setHandle(coleccion.getHandle());
        dto.setFechaYHoraDeActualizacion(coleccion.getFechaYHoraDeActualizacion());
        dto.setConsenso(coleccion.getConsenso().getNombre());
        return dto;
    }

    public static Fuente fuenteDTOtoFuente(FuenteInputDTO fuenteDTO){
        return new Fuente(fuenteDTO.getIp(), fuenteDTO.getPuerto(), fuenteDTO.getId());
    }

    public static FuenteDTO fuenteToFuenteDTO(Fuente fuente){
        return new FuenteDTO(fuente.getId(), fuente.getIp(), fuente.getPuerto(),fuente.getOrigenHechos());
    }

}
