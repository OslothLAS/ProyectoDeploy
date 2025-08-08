package ar.utn.frba.ddsi.agregador.models.repositories.impl;

import ar.utn.frba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import entities.solicitudes.PosibleEstadoSolicitud;
import entities.solicitudes.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SolicitudMemoryRepository implements ISolicitudEliminacionRepository {
    private final Map<Long, SolicitudEliminacion> solicitudes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @Override
    public Long save(SolicitudEliminacion solicitud) {
        if(solicitud.getId() == null){
            solicitud.setId(idGenerator.getAndIncrement());
            solicitudes.put(solicitud.getId(), solicitud);
        }else{
            solicitudes.put(solicitud.getId(), solicitud);
        }

        return solicitud.getId();
    }

    @Override
    public Optional<SolicitudEliminacion> findById(Long id) {
        return Optional.ofNullable(solicitudes.get(id));
    }

    @Override
    public List<SolicitudEliminacion> findAll() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public List<SolicitudEliminacion> findByEstado(PosibleEstadoSolicitud estado) {
        return solicitudes.values().stream()
                .filter(solicitud -> estado.equals(solicitud.getEstado()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSolicitud(SolicitudEliminacion solicitud) {
        solicitudes.remove(solicitud.getId());
    }
}