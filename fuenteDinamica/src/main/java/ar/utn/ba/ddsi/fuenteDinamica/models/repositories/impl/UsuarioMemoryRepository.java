package ar.utn.ba.ddsi.fuenteDinamica.models.repositories.impl;

import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IUsuarioRepository;
import entities.usuarios.Usuario;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

//solo para Admin o Contribuyente, visualizadores no se persisten (por ahora)
@Repository
public class UsuarioMemoryRepository implements IUsuarioRepository {
    private final Map<Long, Usuario> usuarios = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void save(Usuario usuario){
        if(usuario.getId() == null){
            usuario.setId(idGenerator.getAndIncrement());
            usuarios.put(usuario.getId(), usuario);
        }else{
            usuarios.put(usuario.getId(), usuario);
        }
    }
    @Override
    public Optional<Usuario> findById(Long id){
        return Optional.ofNullable(usuarios.get(id));
    }
}
