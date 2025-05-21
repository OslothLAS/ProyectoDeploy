package ar.utn.ba.ddsi.fuenteDinamica.services.impl;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.DatosHechos;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Ubicacion;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.usuarios.Contribuyente;
import config.HechoProperties;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class HechoService implements IHechoService {
    @Autowired  // ← Opcional en Spring Boot
    private HechoProperties hechoProperties;
    private final IHechoRepository hechoRepository;

    public HechoService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }
    @Override
    public void crearHecho(HechoDTO hechoDTO) {
        Contribuyente cont = new Contribuyente(hechoDTO.getId(), hechoDTO.getNombre(), hechoDTO.getApellido(),hechoDTO.getFechaHecho());
        Ubicacion ubi = new Ubicacion(hechoDTO.getLatitud(), hechoDTO.getLongitud());
        DatosHechos datos = new DatosHechos(hechoDTO.getTitulo(),hechoDTO.getDescripcion(),hechoDTO.getCategoria(),ubi,hechoDTO.getFechaHecho(), LocalDate.now());

        if(cont.getRegistrado()) {
            Hecho hecho = Hecho.create(datos,cont,hechoDTO.getMultimedia(), hechoDTO.getMostrarDatos());
            hecho.setEsEditable(true);
            hecho.setPlazoEdicion(Duration.ofDays(10));//(hechoProperties.getPlazoEdicionDias()) no andaaa
            this.hechoRepository.save(hecho);
        }else{
            Hecho hecho = Hecho.create(datos,cont,hechoDTO.getMostrarDatos());
            hecho.setEsEditable(false);
            this.hechoRepository.save(hecho);
        }
    }

    @Override
    public void editarHecho(Long idHecho, HechoDTO dto) throws Exception {
        Hecho hecho = hechoRepository.findById(idHecho)
                .orElseThrow(Exception::new);
       /* if(!hecho.getUsuario().getId().equals(dto.getUsuario().getId())) {
            throw new Exception("Solo el autor del hecho puede modificarlo");
        }*/
        if (!hecho.getUsuario().getRegistrado()) {
            throw new Exception("Usuarios anonimos no pueden editar hechos");
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
    public List<Hecho> obtenerTodos(){
        return hechoRepository.findAll();
    }
}
