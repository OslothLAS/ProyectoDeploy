package ar.utn.ba.ddsi.fuenteProxy.services;

import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IMetamapaService {
    List<Hecho> getHechos();
    List<Hecho> getHechosXcategoria(String categoria);
    List<Hecho> getHechosXmetamapa(Long id);
}
