package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import entities.usuarios.Usuario;
import java.util.Optional;

public interface IUsuarioRepository {
    void save(Usuario usuario);
    Optional<Usuario> findById(Long id);
}
