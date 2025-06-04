package ar.utn.frba.ddsi.agregador.models.repositories.impl;

import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionMemoryRepository;
import entities.colecciones.Coleccion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColeccionMemoryRepository implements IColeccionMemoryRepository {
    private List<Coleccion> colecciones;

    @Override
    public List<Coleccion> getColecciones() {
        return this.colecciones;
    }

    @Override
    public void actualizarColecciones(List<Coleccion> colecciones) {
        Set<Coleccion> coleccionActualizada = new HashSet<>();
        coleccionActualizada.addAll(this.colecciones);
        coleccionActualizada.addAll(colecciones);
        this.colecciones = coleccionActualizada.stream().toList();
        this.setearUltimaActualizacion();
    }

    private void setearUltimaActualizacion(){
        this.colecciones.forEach(coleccion -> coleccion.setFechaYHoraDeActualizacion(LocalDateTime.now()));
    }


}
