package ar.utn.frba.ddsi.agregador.models.repositories;
import entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
}
