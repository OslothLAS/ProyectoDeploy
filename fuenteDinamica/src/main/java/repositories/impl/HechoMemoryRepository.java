package repositories.impl;

import entities.hechos.Hecho;
import repositories.IHechoRepository;

import java.util.List;

public class HechoMemoryRepository implements IHechoRepository {
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
}
