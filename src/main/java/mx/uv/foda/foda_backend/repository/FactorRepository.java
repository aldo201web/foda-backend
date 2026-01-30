package mx.uv.foda.foda_backend.repository;

import java.util.List;
import mx.uv.foda.foda_backend.entitys.Factor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FactorRepository extends JpaRepository<Factor, Long> {

    List<Factor> findByEmpresaIdOrderByCreatedAtDesc(Long empresaId);

}