package ar.utn.ba.ddsi.fuenteProxy.services;

import entities.hechos.Hecho;
import java.util.List;
import java.util.Map;

public interface IApiService {
    List<Hecho> getHechos(Map<String, String> filtros);
}
