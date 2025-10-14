package ar.utn.ba.ddsi.fuenteProxy.models.repositories;

import ar.utn.ba.ddsi.fuenteProxy.models.entities.Metamapa;
import java.util.List;

public interface IRepositoryMetamapa {
    void save(Metamapa metamapa);
    List<Metamapa> findAll();
    Metamapa findById(Long Id);
}
