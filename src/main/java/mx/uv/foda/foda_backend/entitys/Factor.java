package mx.uv.foda.foda_backend.entitys;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "factores")
public class Factor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; 
    // Fortaleza | Debilidad | Oportunidad | Amenaza

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private int impacto; // 1 a 5

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ===== Constructores =====
    public Factor() {
        this.createdAt = LocalDateTime.now();
    }

    public Factor(String tipo, String descripcion, int impacto, Long empresaId) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.impacto = impacto;
        this.empresaId = empresaId;
        this.createdAt = LocalDateTime.now();
    }

    // ===== Getters y Setters =====
    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImpacto() {
        return impacto;
    }

    public void setImpacto(int impacto) {
        this.impacto = impacto;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}