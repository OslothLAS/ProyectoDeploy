package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.CriterioInputDTO;



import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import entities.Importador;
import entities.colecciones.Coleccion;
import entities.colecciones.Fuente;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.criteriosDePertenencia.CriterioPorCategoria;
import entities.criteriosDePertenencia.CriterioPorFecha;
import entities.hechos.Hecho;
import org.springframework.stereotype.Service;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    private final IHechoRepository hechoRepository;

    public ColeccionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }


    @Override
    public List<Hecho> createColeccion(ColeccionInputDTO coleccionDTO) {
        List<Fuente> importadores = coleccionDTO.getImportadores().stream()
                .map(Fuente::new)
                .toList();

        List<CriterioDePertenencia> criterios = coleccionDTO.getCriterios().stream()
                .map(this::mapearCriterioDTO)
                .toList();

        Coleccion coleccion = new Coleccion(
                coleccionDTO.getTitulo(),
                coleccionDTO.getDescripcion(),
                importadores,
                criterios
        );

        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores);

        List<Hecho> hechosValidos = filtrarHechosValidos(todosLosHechos);

        return hechosValidos.stream()
                .filter(coleccion::cumpleCriterios)
                .peek(h -> h.addColeccion(coleccion))
                .toList();
    }

    private CriterioDePertenencia mapearCriterioDTO(CriterioInputDTO dto) {
        return switch (dto.getTipo().toLowerCase()) {
            case "categoria" -> new CriterioPorCategoria(dto.getCategoria());
            case "fecha" -> new CriterioPorFecha(dto.getFechaInicio(), dto.getFechaFin());
            default -> throw new IllegalArgumentException("Tipo de criterio desconocido: " + dto.getTipo());
        };
    }

    private List<Hecho> tomarHechosImportadores(List<Fuente> importadores) {
        return importadores.stream()
                .flatMap(i -> i.obtenerHechos().stream())
                .toList();
    }




    @Override
    public List<Hecho> getColeccion(String idColeccion) {
        List<Hecho> todosLosHechos = filtrarHechosValidos(hechoRepository.findAll());

        return todosLosHechos.stream()
                .filter(hecho -> hecho.getColecciones().stream()
                        .anyMatch(coleccion -> coleccion.getHandle().getValue().equals(idColeccion)))
                .collect(Collectors.toList());
    }

    public void actualizarHechos(){
        List<Hecho> hechos = filtrarHechosValidos(hechoRepository.findAll());
        List<Coleccion> colecciones = this.traerColecciones();
        colecciones.forEach(coleccion -> coleccion.filtrarHechos(hechos));
        hechos.forEach(hecho -> hechoRepository.save(hecho));
    }

    //Crea una lista con todas las colecciones existentes para usarse (por el momento)
    //en actualizarHechos
    public List<Coleccion> traerColecciones(){
        Set<Coleccion> colecciones = new HashSet<>();
        List<Hecho> hechos = hechoRepository.findAll();
        hechos.forEach(hecho -> colecciones.addAll(hecho.getColecciones()));
        return new ArrayList<>(colecciones);
    }

    //Esto es solo para agregar los hechos validos
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(Hecho::getEsValido).collect(Collectors.toList());
    }



}
