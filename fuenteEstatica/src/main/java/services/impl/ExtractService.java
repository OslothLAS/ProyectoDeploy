package services.impl;

import entities.ImportadorHechos;
import entities.hechos.Hecho;
import org.springframework.stereotype.Service;
import services.IExtractService;

import java.util.List;

@Service
public class ExtractService implements IExtractService {
    private final ImportadorHechos importador;

    public ExtractService(ImportadorHechos importador) {
        this.importador = importador;
    }

    @Override
    public List<Hecho> getHechos() {
        return importador.obtenerHechos();
    }
}
