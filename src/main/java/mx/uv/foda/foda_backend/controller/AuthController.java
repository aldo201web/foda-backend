package mx.uv.foda.foda_backend.controller;


import mx.uv.foda.foda_backend.dto.LoginRequest;
import mx.uv.foda.foda_backend.dto.LoginResponse;
import mx.uv.foda.foda_backend.entitys.Usuario;
import mx.uv.foda.foda_backend.repository.UsuarioRepository;
import mx.uv.foda.foda_backend.security.AuthService;
import mx.uv.foda.foda_backend.security.AuthSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    public AuthController(UsuarioRepository usuarioRepository, AuthService authService) {
        this.usuarioRepository = usuarioRepository;
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest body) {
        if (body.getUsuario() == null || body.getContrasena() == null) {
            return LoginResponse.fail("Faltan datos");
        }

        Usuario u = usuarioRepository.findByUsuario(body.getUsuario()).orElse(null);
        if (u == null) return LoginResponse.fail("Usuario no existe");

        // TEXTO PLANO (escolar)
        if (!u.getPassword().equals(body.getContrasena())) {
            return LoginResponse.fail("Contraseña incorrecta");
        }

        // Empresa fija = 1 para mínimo funcional
        Long empresaId = 1L;

        String token = authService.createToken(new AuthSession(
                u.getId(),
                u.getNombre(),
                u.getRol(),
                empresaId
        ));

        return LoginResponse.ok(token, u.getId(), u.getNombre(), u.getRol(), empresaId);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = AuthService.extractBearer(authHeader);
        if (token != null) authService.logout(token);
        return "OK";
    }
}