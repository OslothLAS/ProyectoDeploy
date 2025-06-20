package ar.utn.frba.ddsi.agregador.navegacion;

import entities.colecciones.Coleccion;
import entities.hechos.Hecho;

import java.util.List;

public class NavegacionIrrestricaStrategy implements NavegacionStrategy{
    @Override
    public List<Hecho> navegar(Coleccion coleccion, List<Hecho> hechos) {
        return hechos; // Sin filtrar, retorna todos
    }
}
