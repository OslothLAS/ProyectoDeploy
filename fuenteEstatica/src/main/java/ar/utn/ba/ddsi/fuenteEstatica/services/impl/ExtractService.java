package ar.utn.ba.ddsi.fuenteEstatica.services.impl;

import ar.utn.ba.ddsi.fuenteEstatica.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.ba.ddsi.fuenteEstatica.entities.criteriosDePertenencia.CriterioDePertenenciaFactory;
import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteEstatica.entities.ImportadorHechos;
import org.springframework.stereotype.Service;
import ar.utn.ba.ddsi.fuenteEstatica.services.IExtractService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExtractService implements IExtractService {
    private final ImportadorHechos importador;

    public ExtractService(ImportadorHechos importador) {
        this.importador = importador;
    }

    @Override
    public List<Hecho> getHechos(Map<String, String> filtros) {
        List<CriterioDePertenencia> criterios = CriterioDePertenenciaFactory.crearCriterios(filtros);

        List<Hecho> hechos = importador.obtenerHechos();

        return hechos.stream()
                .filter(Hecho::getEsValido)
                .filter(hecho -> criterios.isEmpty() ||
                        criterios.stream().allMatch(c -> c.cumpleCriterio(hecho)))
                .collect(Collectors.toList());
    }

    public void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion){
        importador.invalidarHechoPorTituloYDescripcion(titulo, descripcion);
    }
}
