package ar.utn.ba.ddsi.fuenteProxy.services;

import entities.hechos.Hecho;

import java.util.List;

public interface IApiService {
    List<Hecho> getHechos();
}
