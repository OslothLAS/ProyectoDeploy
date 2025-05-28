package ar.utn.ba.ddsi.fuenteProxy.repositories.impl;

import ar.utn.ba.ddsi.fuenteProxy.repositories.IRepositoryMetamapa;
import entities.Metamapa;
import entities.hechos.Hecho;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RepositoryMetamapa implements IRepositoryMetamapa {

        private final Map<Long, Metamapa> metamapas = new HashMap<>();
        private final AtomicLong idGenerator = new AtomicLong(1);

        @Override
        public void save(Metamapa metamapa) {
            if(metamapa.getId() == null){
                metamapa.setId(idGenerator.getAndIncrement());
            }
                metamapas.put(metamapa.getId(), metamapa);
            }
        @Override
        public List<Metamapa> findAll(){
            return new ArrayList<>(metamapas.values());
        }

        @Override
        public Metamapa findById(Long Id){
            return metamapas.get(Id);
        }
}
