package ar.utn.frba.ddsi.agregador.models.repositories.impl;


import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.Hecho;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechoMemoryRepository implements IHechoRepository {

    private final Map<Long, Hecho> hechos = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void save(Hecho hecho) {
        if(hecho.getId() == null){
            hecho.setId(idGenerator.getAndIncrement());
            hechos.put(hecho.getId(), hecho);
        }else{
            hechos.put(hecho.getId(), hecho);
        }
    }

    @Override
    public Hecho findById(Long id){
        return hechos.get(id);
    }

    @Override
    public List<Hecho> findAll(){
        return new ArrayList<>(hechos.values());
    }

    @Override
    public List<Hecho> findSegunCriterios(List<CriterioDePertenencia> criterios) {
        return List.of(); //TODO
    }


}
