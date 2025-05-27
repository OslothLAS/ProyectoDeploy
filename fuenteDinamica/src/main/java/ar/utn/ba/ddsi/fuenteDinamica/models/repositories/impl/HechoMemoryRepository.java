package ar.utn.ba.ddsi.fuenteDinamica.models.repositories.impl;


import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import entities.hechos.Hecho;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechoMemoryRepository implements IHechoRepository {
    private final Map<Long, Hecho> hechos = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    //private final Map<Hecho, Long> hechoToIdMap = new HashMap<>();

    @Override
    public void save(Hecho hecho) {
        if(hecho.getId() == null){
            hecho.setId(idGenerator.getAndIncrement());
            hechos.put(hecho.getId(), hecho);
        }else{
            hechos.put(hecho.getId(), hecho);
        }
    }


    /*public void save(Hecho hecho) {
        if (hechoToIdMap.containsKey(hecho)) {
            Long existingId = hechoToIdMap.get(hecho);
            hechos.put(existingId, hecho);
        }else {
            Long newId = idGenerator.getAndIncrement();
            hechos.put(newId, hecho);
            hechoToIdMap.put(hecho, newId);
        }
    }*/

    @Override
    public Optional<Hecho> findById(Long id){
        return Optional.ofNullable(hechos.get(id));
    }

    @Override
    public List<Hecho> findAll(){
        return new ArrayList<>(hechos.values());
    }
}
