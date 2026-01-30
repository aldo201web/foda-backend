package mx.uv.foda.foda_backend.repository;

import java.util.Optional;
import mx.uv.foda.foda_backend.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario);

}