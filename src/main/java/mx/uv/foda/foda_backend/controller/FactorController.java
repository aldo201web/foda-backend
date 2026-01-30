package mx.uv.foda.foda_backend.controller;

import java.util.List;
import mx.uv.foda.foda_backend.dto.FactorRequest;
import mx.uv.foda.foda_backend.entitys.Factor;
import mx.uv.foda.foda_backend.repository.FactorRepository;
import mx.uv.foda.foda_backend.security.AuthService;
import mx.uv.foda.foda_backend.security.AuthSession;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factores")
public class FactorController {

    private final FactorRepository factorRepository;
    private final AuthService authService;

    public FactorController(FactorRepository factorRepository, AuthService authService) {
        this.factorRepository = factorRepository;
        this.authService = authService;
    }

    private AuthSession requireSession(String authHeader) {
        String token = AuthService.extractBearer(authHeader);
        if (token == null) return null;
        return authService.getSession(token);
    }

    @GetMapping
    public Object listar(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        AuthSession s = requireSession(authHeader);
        if (s == null) return "No autorizado";

        List<Factor> lista = factorRepository.findByEmpresaIdOrderByCreatedAtDesc(s.getEmpresaId());
        return lista;
    }

    @PostMapping
    public Object crear(@RequestHeader(value = "Authorization", required = false) String authHeader,
                        @RequestBody FactorRequest body) {
        AuthSession s = requireSession(authHeader);
        if (s == null) return "No autorizado";

        if (body.getTipo() == null || body.getTipo().trim().isEmpty()) return "Tipo requerido";
        if (body.getDescripcion() == null || body.getDescripcion().trim().isEmpty()) return "Descripción requerida";
        if (body.getImpacto() < 1 || body.getImpacto() > 5) return "Impacto debe ser 1 a 5";

        Factor f = new Factor(body.getTipo().trim(), body.getDescripcion().trim(), body.getImpacto(), s.getEmpresaId());
        return factorRepository.save(f);
    }

    @PutMapping("/{id}")
    public Object editar(@RequestHeader(value = "Authorization", required = false) String authHeader,
                         @PathVariable Long id,
                         @RequestBody FactorRequest body) {
        AuthSession s = requireSession(authHeader);
        if (s == null) return "No autorizado";

        Factor f = factorRepository.findById(id).orElse(null);
        if (f == null) return "No existe";

        if (!f.getEmpresaId().equals(s.getEmpresaId())) return "No autorizado";

        f.setTipo(body.getTipo() == null ? f.getTipo() : body.getTipo().trim());
        f.setDescripcion(body.getDescripcion() == null ? f.getDescripcion() : body.getDescripcion().trim());
        if (body.getImpacto() >= 1 && body.getImpacto() <= 5) f.setImpacto(body.getImpacto());

        return factorRepository.save(f);
    }

    @DeleteMapping("/{id}")
    public Object eliminar(@RequestHeader(value = "Authorization", required = false) String authHeader,
                           @PathVariable Long id) {
        AuthSession s = requireSession(authHeader);
        if (s == null) return "No autorizado";

        // Rol: solo admin puede eliminar
        if (!"admin".equalsIgnoreCase(s.getRol())) return "Prohibido (solo admin)";

        Factor f = factorRepository.findById(id).orElse(null);
        if (f == null) return "No existe";
        if (!f.getEmpresaId().equals(s.getEmpresaId())) return "No autorizado";

        factorRepository.deleteById(id);
        return "OK";
    }
}