package ar.utn.ba.ddsi.fuenteProxy.repositories;

import entities.Metamapa;
import entities.hechos.Hecho;

import java.util.List;
import java.util.Optional;

public interface IRepositoryMetamapa {
    void save(Metamapa metamapa);
    List<Metamapa> findAll();
}
