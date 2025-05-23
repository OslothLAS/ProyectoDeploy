package ar.utn.ba.ddsi.fuenteProxy.repository;


import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import entities.hechos.DatosHechos;

import java.util.List;

public interface IHechoRepository {
    void save(HechoDto hecho);
    List<HechoDto> findAll();
}

