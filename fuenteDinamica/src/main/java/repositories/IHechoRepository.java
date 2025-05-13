package repositories;

import entities.hechos.Hecho;

public interface IHechoRepository {
    void save(Hecho hecho);
    void edit(Hecho hecho);
}
