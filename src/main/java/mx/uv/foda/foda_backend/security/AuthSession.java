package mx.uv.foda.foda_backend.security;

public class AuthSession {
    private final Long userId;
    private final String userName;
    private final String rol;
    private final Long empresaId;

    public AuthSession(Long userId, String userName, String rol, Long empresaId) {
        this.userId = userId;
        this.userName = userName;
        this.rol = rol;
        this.empresaId = empresaId;
    }

    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getRol() { return rol; }
    public Long getEmpresaId() { return empresaId; }
}