package mx.uv.foda.foda_backend.dto;

public class FactorRequest {
    private String tipo;
    private String descripcion;
    private int impacto;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getImpacto() { return impacto; }
    public void setImpacto(int impacto) { this.impacto = impacto; }
}