package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.hecho.HechoDto;
import ar.utn.ba.ddsi.fuenteProxy.mappers.HechoMapper;
import ar.utn.ba.ddsi.fuenteProxy.repositories.IRepositoryMetamapa;
import ar.utn.ba.ddsi.fuenteProxy.services.IHechoService;
import entities.Metamapa;
import entities.colecciones.Handle;
import entities.hechos.Hecho;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {
    private final WebClient webClient;
    private final IRepositoryMetamapa metamapaRepository;

    private static final String HECHOS_PATH = "/api/hechos";


    public HechoService(IRepositoryMetamapa metamapaRepository) {
        this.metamapaRepository = metamapaRepository;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public List<Hecho> getHechos() {
        return metamapaRepository.findAll().stream()
                .flatMap(metamapa -> {
                    String fullUrl = metamapa.getUrl() + HECHOS_PATH;

                    List<Hecho> hechos = fetchHechosFromUrl(fullUrl);

                    return hechos.stream();
                })
                .toList();
    }

    @Override
    public List<Hecho> getHechosXcategoriaXcolecciones(String categoria, Long metamapa, Handle id_coleccion) {
        return getHechosXcoleccionXmetamapa(id_coleccion, metamapa).stream()
                .filter(hecho -> hecho.getDatosHechos() != null)
                .filter(hecho -> hecho.getDatosHechos().getCategoria() != null)
                .filter(hecho -> hecho.getDatosHechos().getCategoria().equalsIgnoreCase(categoria.trim()))
                .toList();
    }


    @Override
    public List<Hecho> getHechosXmetamapa(Long id) {
        Metamapa metamapa = metamapaRepository.findById(id);
        String fullUrl = metamapa.getUrl() + HECHOS_PATH;
        return fetchHechosFromUrl(fullUrl);
    }

    @Override
    public List<Hecho> getHechosXcoleccionXmetamapa(Handle id_coleccion, Long metamapa) {
        return getHechosXmetamapa(metamapa).stream()
                .filter(hecho -> hecho.getDatosHechos() != null)
                .filter(hecho -> hecho.getColecciones() != null)
                .filter(hecho -> hecho.getColecciones().contains(id_coleccion))
                .toList();
    }

    @Override
    public List<Hecho> getHechosXColeccionXmetampaXModoNavegacion(String modoNavegacion, Handle id_coleccion, Long metamapa) {
        List<Hecho> hechosColeccionMetamapa = getHechosXcoleccionXmetamapa(id_coleccion, metamapa);

        if (modoNavegacion.equals("irrestricta")) {
            return hechosColeccionMetamapa;
        } else if (modoNavegacion.equals("curada")) {
            return hechosColeccionMetamapa.stream()
                    .filter(Hecho::getEsConsensuado)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // O lanzar una excepción si no es un modo válido
        }
    }

    private List<Hecho> fetchHechosFromUrl(String url) {
        try {
            List<HechoDto> hechosDto = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(HechoDto.class)
                    .collectList()
                    .block();
            if (hechosDto == null) return List.of();
            return hechosDto.stream()
                    .map(HechoMapper::mapHechoDtoToHecho)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
}
