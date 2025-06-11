package ar.utn.frba.ddsi.agregador.models.repositories.impl;

import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import entities.colecciones.Coleccion;
import entities.colecciones.Handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColeccionMemoryRepository implements IColeccionRepository {
    private final Map<Handle, Coleccion> colecciones = new HashMap<>();
    @Override
    public void save(Coleccion coleccion) {
        colecciones.put(coleccion.getHandle(), coleccion);
    }

    @Override
    public Coleccion findById(String handleValue) {
        for (Handle handle : colecciones.keySet()) {
            if (handle.getValue().equals(handleValue)) {
                return colecciones.get(handle);
            }
        }
        return null;
    }

    @Override
    public List<Coleccion> findAll() {
        return new ArrayList<>(colecciones.values());
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
