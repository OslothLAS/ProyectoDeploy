package models.repositories.impl;

import models.entities.solicitudes.EstadoSolicitudEliminacion;
import models.entities.solicitudes.SolicitudEliminacion;
import models.repositories.ISolicitudEliminacionRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SolicitudMemoryRepository implements ISolicitudEliminacionRepository {
    private final Map<Long, SolicitudEliminacion> solicitudes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @Override
    public void save(SolicitudEliminacion solicitud) {
        if(solicitud.getId() == null){
            solicitud.setId(idGenerator.getAndIncrement());
            solicitudes.put(solicitud.getId(), solicitud);
        }else{
            solicitudes.put(solicitud.getId(), solicitud);
        }
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
    public List<SolicitudEliminacion> findByEstado(EstadoSolicitudEliminacion estado) {
        return solicitudes.values().stream()
                .filter(solicitud -> estado.equals(solicitud.getEstado()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSolicitude(SolicitudEliminacion solicitud) {

    }
}
