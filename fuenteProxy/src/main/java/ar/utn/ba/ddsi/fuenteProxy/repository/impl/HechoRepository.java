package ar.utn.ba.ddsi.fuenteProxy.repository.impl;


import entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteProxy.repository.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechoRepository implements IHechoRepository {

    private final List<Hecho> hechos = new ArrayList<>();

    @Override
    public void save(Hecho hecho) {
        hechos.add(hecho);
    }

    @Override
    public List<Hecho> findAll() {
        return new ArrayList<>(hechos); // protegés la colección original
    }
}

