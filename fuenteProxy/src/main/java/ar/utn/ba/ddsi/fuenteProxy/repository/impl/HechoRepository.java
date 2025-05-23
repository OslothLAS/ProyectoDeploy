package ar.utn.ba.ddsi.fuenteProxy.repository.impl;


import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteProxy.repository.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechoRepository implements IHechoRepository {

    private final List<HechoDto> hechos = new ArrayList<>();

    @Override
    public void save(HechoDto hecho) {
        hechos.add(hecho);
    }

    @Override
    public List<HechoDto> findAll() {
        return new ArrayList<>(hechos); // protegés la colección original
    }
}

