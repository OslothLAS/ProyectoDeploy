package ar.utn.ba.ddsi.fuenteProxy.repository;


import entities.hechos.Hecho;

import java.util.List;

public interface IHechoRepository {
    void save(Hecho hecho);
    List<Hecho> findAll();
}

