package models.repositories.impl;

import entities.hechos.Hecho;
import models.entities.solicitudes.SolicitudEliminacion;
import models.repositories.ISolicitudEliminacionRepository;

import java.util.List;

public class SolicitudEliminacionRepository implements ISolicitudEliminacionRepository {

    private List<SolicitudEliminacion> solicitudes;

    @Override
    public List<SolicitudEliminacion> findAllSolicitudes() {
        return this.solicitudes;
    }

    @Override
    public void saveSolicitude(SolicitudEliminacion solicitud) {this.solicitudes.add(solicitud);}

    @Override
    public void deleteSolicitude(SolicitudEliminacion solicitud) {

    }


}
