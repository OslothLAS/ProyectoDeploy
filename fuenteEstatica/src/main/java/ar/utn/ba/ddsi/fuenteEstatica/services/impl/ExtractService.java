package ar.utn.ba.ddsi.fuenteEstatica.services.impl;

import ar.utn.ba.ddsi.fuenteEstatica.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.ba.ddsi.fuenteEstatica.entities.criteriosDePertenencia.CriterioDePertenenciaFactory;
import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteEstatica.entities.ImportadorHechos;
import org.springframework.stereotype.Service;
import ar.utn.ba.ddsi.fuenteEstatica.services.IExtractService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ExtractService implements IExtractService {
    private final ImportadorHechos importador;

    private List<Hecho> hechosEnMemoria = new ArrayList<>();
    private final Set<String> clavesHechosInvalidos = ConcurrentHashMap.newKeySet();

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

    @Override
    public List<Hecho> getHechosMemoria(Map<String, String> filtros) {
        List<CriterioDePertenencia> criterios = CriterioDePertenenciaFactory.crearCriterios(filtros);

        // Aplica invalidaciones a la lista en memoria
        hechosEnMemoria.forEach(hecho -> {
            boolean valido = !clavesHechosInvalidos.contains(importador.claveHecho(hecho.getTitulo(), hecho.getDescripcion()));
            hecho.setEsValido(valido);
        });

        // Filtra la lista en memoria
        return hechosEnMemoria.stream()
                .filter(Hecho::getEsValido)
                .filter(hecho -> criterios.isEmpty() ||
                        criterios.stream().allMatch(c -> c.cumpleCriterio(hecho)))
                .collect(Collectors.toList());
    }

    public void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion){
        importador.invalidarHechoPorTituloYDescripcion(titulo, descripcion);
    }

    @Override
    public List<Hecho> importarHechosDesdeArchivo(InputStream inputStream, String fileName) {
        String extension = utils.ExtensionReader.getFileExtension(fileName);

        // Llama al importador (stateless) para procesar el stream
        List<Hecho> nuevosHechos = importador.procesarStreamDeArchivo(inputStream, extension);

        // Guarda los hechos en la memoria de ESTE servicio
        this.hechosEnMemoria = nuevosHechos;

        // Limpia las invalidaciones anteriores
        this.clavesHechosInvalidos.clear();

        return this.hechosEnMemoria;
    }

}
