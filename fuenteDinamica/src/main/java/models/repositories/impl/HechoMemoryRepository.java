package models.repositories.impl;

import lombok.Getter;
import models.entities.hechos.Hecho;
import models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class HechoMemoryRepository implements IHechoRepository {

    @Getter
    private List<Hecho> hechos;

    @Override
    public void save(Hecho hecho) {
        this.hechos.add(hecho);
    }

    @Override
    public void edit(Hecho hecho) {
        if(hechos.contains(hecho)){
            this.hechos.set(hechos.indexOf(hecho), hecho);
        }
        //TODO (gadi)
    }

    @Override
    public List<Hecho> findAll() {
        return List.of();
    }
}
