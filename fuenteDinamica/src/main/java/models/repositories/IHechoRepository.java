package models.repositories;

import models.entities.hechos.Hecho;

import java.util.List;

public interface IHechoRepository {
    void save(Hecho hecho);
    void edit(Hecho hecho);
    List<Hecho> findAll();
}
