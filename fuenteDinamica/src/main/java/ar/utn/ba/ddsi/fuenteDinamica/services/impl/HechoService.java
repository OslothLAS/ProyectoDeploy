package ar.utn.ba.ddsi.fuenteDinamica.services.impl;


import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IUsuarioRepository;
import config.HechoProperties;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.factories.CriterioDePertenenciaFactory;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class HechoService implements IHechoService {
    @Autowired
    private HechoProperties hechoProperties;
    private final IHechoRepository hechoRepository;
    private final IUsuarioRepository usuarioRepository;

    public HechoService(IHechoRepository hechoRepository,IUsuarioRepository usuarioRepository) {
        this.hechoRepository = hechoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    public void crearHecho(HechoInputDTO hechoDTO) {
        DatosHechos datos = hechoDTO.getDatosHechos();

        if(hechoDTO.getId() != null) { //si tiene ID => es contribuyente
            Usuario usuario = usuarioRepository.findById(hechoDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con ID: " + hechoDTO.getId()));
            Hecho hecho = Hecho.create(datos, usuario,hechoDTO.getMultimedia(), hechoDTO.getMostrarDatos());
            hecho.setEsEditable(true);
            hecho.setPlazoEdicion(Duration.ofDays(hechoProperties.getPlazoEdicionDias()));
            this.hechoRepository.save(hecho);
        } else{
//          Visualizador visualizador = new Visualizador(hechoDTO.getNombre(),hechoDTO.getApellido(),hechoDTO.getFechaDeNacimiento());
            Hecho hecho = Hecho.create(datos);
            hecho.setEsEditable(false);
            this.hechoRepository.save(hecho);
        }
    }

    @Override
    public void editarHecho(Long idHecho, HechoInputDTO dto) throws Exception {
        Hecho hecho = hechoRepository.findById(idHecho)
                .orElseThrow(Exception::new);
/*
        if (!hecho.getAutor().getRegistrado()) {
            throw new Exception("Usuarios anonimos no pueden editar hechos");
        }
*/
        if(!hecho.getAutor().getId().equals(dto.getId())) {
            throw new Exception("Solo el autor del hecho puede modificarlo");
        }

        if (!hecho.esEditable()) {
            throw new Exception("El plazo de edicion ha expirado");
        }

        if (dto.getDatosHechos().getTitulo() != null) {
            hecho.getDatosHechos().setTitulo(dto.getDatosHechos().getTitulo());
        }

        if (dto.getDatosHechos().getDescripcion() != null) {
            hecho.getDatosHechos().setDescripcion(dto.getDatosHechos().getDescripcion());
        }

        if (dto.getDatosHechos().getCategoria() != null) {
            hecho.getDatosHechos().setCategoria(dto.getDatosHechos().getCategoria());
        }

//        if (dto.getLatitud() != null) {
//            //hecho.getDatosHechos().setUbicacion(dto.getUbicacion());
//TODO        }

        if (dto.getDatosHechos().getFechaHecho() != null) {
            hecho.getDatosHechos().setFechaHecho(dto.getDatosHechos().getFechaHecho());
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
                .filter(Hecho::getEsValido)
                .filter(hecho -> criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho)))
                .collect(Collectors.toList());
    }
    public void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion) {
        Optional<Hecho> hechoInvalido = hechoRepository.findByDatosHechosTituloAndDatosHechosDescripcion(titulo, descripcion);
        hechoInvalido.ifPresent(hecho -> {
            hecho.setEsValido(false);
            hechoRepository.save(hecho);
        });
    }
}