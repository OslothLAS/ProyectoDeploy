package ar.utn.ba.ddsi.fuenteDinamica.services.impl;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.TokenInfo;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.exceptions.UnauthorizedActionException;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.criteriosDePertenencia.CriterioDePertenenciaFactory;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Origen;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Provincia;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.usuarios.Rol;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.ICategoriaRepository;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IProvinciaRepository;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import ar.utn.ba.ddsi.fuenteDinamica.utils.HechoUtil;
import ar.utn.ba.ddsi.fuenteDinamica.config.HechoProperties;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static ar.utn.ba.ddsi.fuenteDinamica.utils.HechoUtil.hechoToDTO;
import static ar.utn.ba.ddsi.fuenteDinamica.utils.HechoUtil.hechosToDTO;

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

    public HechoDTO getHechoById(Long id) {
        Optional<Hecho> hecho = hechoRepository.findById(id);
        return hechoToDTO(hecho.get());
    }

    public List<HechoDTO> getAllHechos() {
        List<Hecho> hecho = hechoRepository.findAll();
        return hecho.stream().map(h -> hechoToDTO(h)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    // Servicio
    public HechoOutputDTO crearHecho(HechoDTO hechoDTO, TokenInfo token) {
        Categoria categoria = new Categoria(hechoDTO.getCategoria());
        Categoria categoriaPersistida = categoriaRepository.findByCategoriaNormalizada(categoria.getCategoriaNormalizada())
                .orElseGet(() -> {
                    try {
                        return categoriaRepository.save(categoria);
                    } catch (DataIntegrityViolationException e) {
                        return categoriaRepository.findByCategoriaNormalizada(categoria.getCategoriaNormalizada())
                                .orElseThrow(() -> new IllegalStateException("Error al recuperar categoría existente", e));
                    }
                });

        Provincia provincia = provinciaRepository.findById(
                        hechoDTO.getUbicacion().getLocalidad().getProvincia().getId())
                .orElseThrow(() -> new RuntimeException("No se encontró la provincia"));

        Hecho hecho = HechoUtil.hechoDTOtoHecho(hechoDTO);
        hecho.setCategoria(categoriaPersistida);
        hecho.getUbicacion().getLocalidad().setProvincia(provincia);

        if (token != null && (token.getRol().equals(Rol.ADMIN.name()) || token.getRol().equals(Rol.CONTRIBUYENTE.name()))) {
            hecho.setEsEditable(true);
            hecho.setUsername(token.getUsername());
            hecho.setOrigen(Origen.CONTRIBUYENTE);
        } else {
            hecho.setOrigen(Origen.VISUALIZADOR);
        }
        hecho.setEsValido(true);

        Hecho hechoPersistido = hechoRepository.save(hecho);

        return HechoUtil.hechoToOutputDTO(hechoPersistido);
    }





    @Override
    public void editarHecho(Long idHecho, HechoDTO dto, TokenInfo tokenInfo) throws Exception {
        Hecho hecho = hechoRepository.findById(idHecho)
                .orElseThrow(Exception::new);

        if(hecho.getUsername() != null){
            if(!hecho.getUsername().equals(tokenInfo.getUsername())) {
                throw new UnauthorizedActionException("Solo el autor del hecho puede modificarlo");
            }
        }else{
            if(tokenInfo != null) {
                throw new UnauthorizedActionException("Solo el autor del hecho puede modificarlo");
            }
        }

        if (!hecho.esEditable()) {
            throw new UnauthorizedActionException("El plazo de edicion ha expirado");
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

        if (dto.getUbicacion() != null) {
            var dtoUbic = dto.getUbicacion();

            if (dtoUbic.getLatitud() != null)
                hecho.getUbicacion().setLatitud(dtoUbic.getLatitud());

            if (dtoUbic.getLongitud() != null)
                hecho.getUbicacion().setLongitud(dtoUbic.getLongitud());

            if (dtoUbic.getLocalidad() != null) {
                var dtoLoc = dtoUbic.getLocalidad();

                if (dtoLoc.getNombre() != null)
                    hecho.getUbicacion().getLocalidad().setNombre(dtoLoc.getNombre());

                if (dtoLoc.getProvincia() != null && dtoLoc.getProvincia().getId() != null)
                    hecho.getUbicacion().getLocalidad().getProvincia().setId(dtoLoc.getProvincia().getId());
            }
        }

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
        Optional<Hecho> hechoInvalido = hechoRepository.findByTituloAndDescripcion(titulo, descripcion);
        hechoInvalido.ifPresent(hecho -> {
            hecho.setEsValido(false);
            hechoRepository.save(hecho);
        });
    }

    public List<HechoDTO> getHechosByUsername(String username){
        return hechosToDTO(hechoRepository.findByUsername(username));
    }
}