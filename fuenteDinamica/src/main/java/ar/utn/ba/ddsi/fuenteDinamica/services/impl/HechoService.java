package ar.utn.ba.ddsi.fuenteDinamica.services.impl;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.criteriosDePertenencia.CriterioDePertenenciaFactory;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.ICategoriaRepository;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IProvinciaRepository;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import config.HechoProperties;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {
    @Autowired
    private HechoProperties hechoProperties;
    private final IHechoRepository hechoRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IProvinciaRepository provinciaRepository;

    public HechoService(IHechoRepository hechoRepository, ICategoriaRepository categoriaRepository,IProvinciaRepository provinciaRepository) {
        this.hechoRepository = hechoRepository;
        this.categoriaRepository = categoriaRepository;
        this.provinciaRepository = provinciaRepository;
    }

    @Transactional
    @Override
    public void crearHecho(HechoDTO hechoDTO) {


       /* Categoria categoria = new Categoria(hechoDTO.getCategoria());

        Categoria categoriaPersistida = categoriaRepository.findByCategoriaNormalizada(categoria.getCategoriaNormalizada())
                .orElseGet(() -> {
                    try {
                        return categoriaRepository.save(categoria);
                    } catch (DataIntegrityViolationException e) {
                        return categoriaRepository.findByCategoriaNormalizada(categoria.getCategoriaNormalizada())
                                .orElseThrow(() -> new IllegalStateException("Error al recuperar categoría existente", e));
                    }
                });
        datos.setCategoria(categoriaPersistida);

        Provincia provincia = this.provinciaRepository.findById(hechoDTO.getUbicacion().getLocalidad().getProvincia().getId())
                .orElseThrow(() -> new RuntimeException("No se encontró la provincia"));

        datos.getUbicacion().getLocalidad().setProvincia(provincia);

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
    }*/
}

@Override
public void editarHecho(Long idHecho, HechoDTO dto) throws Exception {
    Hecho hecho = hechoRepository.findById(idHecho)
            .orElseThrow(Exception::new);

    /*if(!hecho.getAutor().getId().equals(dto.getId())) {
        throw new Exception("Solo el autor del hecho puede modificarlo");
    }*/

    if (!hecho.esEditable()) {
        throw new Exception("El plazo de edicion ha expirado");
    }

    if (dto.getTitulo() != null) {
        hecho.setTitulo(dto.getTitulo());
    }

    if (dto.getDescripcion() != null) {
        hecho.setDescripcion(dto.getDescripcion());
    }

    if (dto.getCategoria() != null) {
        hecho.setCategoria(new Categoria(dto.getCategoria()));
    }

//        if (dto.getLatitud() != null) {
//            //hecho.getDatosHechos().setUbicacion(dto.getUbicacion());
//TODO        }

    if (dto.getFechaHecho() != null) {
        hecho.setFechaHecho(dto.getFechaHecho());
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