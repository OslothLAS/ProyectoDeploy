package ar.utn.ba.ddsi.fuenteDinamica.services.impl;


import config.HechoProperties;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.factories.CriterioDePertenenciaFactory;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Ubicacion;
import entities.usuarios.Contribuyente;
import entities.usuarios.Visualizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class HechoService implements IHechoService {
    @Autowired  // ← Opcional en Spring Boot
    private HechoProperties hechoProperties;
    private final IHechoRepository hechoRepository;

    public HechoService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }
    @Override
    public void crearHecho(HechoInputDTO hechoDTO) {
        Ubicacion ubi = new Ubicacion(hechoDTO.getLatitud(), hechoDTO.getLongitud());
        DatosHechos datos = new DatosHechos(hechoDTO.getTitulo(), hechoDTO.getDescripcion(), hechoDTO.getCategoria(), ubi, hechoDTO.getFechaHecho());

        if(hechoDTO.getIdUsuario() != null) { //si tiene ID => es contribuyente
            Contribuyente contribuyente = new Contribuyente(hechoDTO.getIdUsuario(), hechoDTO.getNombre(), hechoDTO.getApellido(), hechoDTO.getFechaHecho());
            Hecho hecho = Hecho.create(datos, contribuyente,hechoDTO.getMultimedia(), hechoDTO.getMostrarDatos());
            hecho.setEsEditable(true);
            hecho.setPlazoEdicion(Duration.ofDays(hechoProperties.getPlazoEdicionDias()));
            this.hechoRepository.save(hecho);
        } else{
            if(hechoDTO.getNombre() == null) {// si la request no tiene ID => es visualizador
                throw new IllegalArgumentException("El campo 'nombre' es obligatorio para usuarios anónimos");
            }
            Visualizador visualizador = new Visualizador(hechoDTO.getNombre(),hechoDTO.getApellido(),hechoDTO.getFechaDeNacimiento());
            Hecho hecho = Hecho.create(datos,visualizador);
            hecho.setEsEditable(false);
            this.hechoRepository.save(hecho);
        }
    }

    @Override
    public void editarHecho(Long idHecho, HechoInputDTO dto) throws Exception {
        Hecho hecho = hechoRepository.findById(idHecho)
                .orElseThrow(Exception::new);

        if (!hecho.getUsuario().getRegistrado()) {
            throw new Exception("Usuarios anonimos no pueden editar hechos");
        }

        if(!hecho.getUsuario().getId().equals(dto.getIdUsuario())) {
            throw new Exception("Solo el autor del hecho puede modificarlo");
        }

        if (!hecho.esEditable()) {
            throw new Exception("El plazo de edicion ha expirado");
        }

        if (dto.getTitulo() != null) {
            hecho.getDatosHechos().setTitulo(dto.getTitulo());
        }

        if (dto.getDescripcion() != null) {
            hecho.getDatosHechos().setDescripcion(dto.getDescripcion());
        }

        if (dto.getCategoria() != null) {
            hecho.getDatosHechos().setCategoria(dto.getCategoria());
        }

//        if (dto.getLatitud() != null) {
//            //hecho.getDatosHechos().setUbicacion(dto.getUbicacion());
//TODO        }

        if (dto.getFechaHecho() != null) {
            hecho.getDatosHechos().setFechaHecho(dto.getFechaHecho());
        }

        if (dto.getMostrarDatos() != null) {
            hecho.setMostrarDatos(dto.getMostrarDatos());
        }

        if (dto.getMultimedia() != null) {
            hecho.setMultimedia(dto.getMultimedia());
        }

        hechoRepository.save(hecho);
    }


    @Override
    public List<Hecho> obtenerTodos(Map<String, String> filtros){
        List<CriterioDePertenencia> criterios = CriterioDePertenenciaFactory.crearCriterios(filtros);
        List<Hecho> hechos = this.hechoRepository.findAll();

        if (criterios.isEmpty()) {
            return hechos;
        }

        return hechos.stream()
                .filter(hecho -> criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho)))
                .collect(Collectors.toList());
    }

}
