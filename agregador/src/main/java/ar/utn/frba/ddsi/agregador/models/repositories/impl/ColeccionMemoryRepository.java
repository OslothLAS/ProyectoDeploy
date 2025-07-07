package ar.utn.frba.ddsi.agregador.models.repositories.impl;

import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import entities.colecciones.Coleccion;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ColeccionMemoryRepository implements IColeccionRepository {
    private final Map<Long, Coleccion> colecciones = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void save(Coleccion coleccion) {
        if(coleccion.getId() == null) {
            coleccion.setId(idGenerator.getAndIncrement());
            colecciones.put(coleccion.getId(), coleccion);
        }
        else{
            colecciones.put(coleccion.getId(), coleccion);
        }
    }

    @Override
    public Optional<Coleccion> findById(Long id){
        return Optional.ofNullable(colecciones.get(id));
    }

    @Override
    public List<Coleccion> findAll() {
        return new ArrayList<>(colecciones.values());
    }


    @Override
    public boolean delete(Long id) {
      return this.colecciones.remove(id) != null;
    }


}
