package mx.uv.foda.foda_backend.config;


import mx.uv.foda.foda_backend.entitys.Usuario;
import mx.uv.foda.foda_backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        // Admin
        usuarioRepository.findByUsuario("admin").orElseGet(() ->
                usuarioRepository.save(new Usuario(
                        "Administrador",
                        "admin",
                        "admin123",   // TEXTO PLANO (escolar)
                        "admin"
                ))
        );

        // User normal
        usuarioRepository.findByUsuario("user").orElseGet(() ->
                usuarioRepository.save(new Usuario(
                        "Usuario Normal",
                        "user",
                        "user123",    // TEXTO PLANO (escolar)
                        "user"
                ))
        );
    }
}