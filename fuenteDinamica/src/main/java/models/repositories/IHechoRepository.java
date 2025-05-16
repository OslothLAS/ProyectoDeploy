package models.repositories;

import models.entities.hechos.Hecho;

public interface IHechoRepository {
    void save(Hecho hecho);
    void edit(Hecho hecho);
}
