package mx.uv.foda.dto;

public class LoginResponse {
    private boolean ok;
    private String error;

    private String token;
    private Long userId;
    private String userName;
    private String rol;
    private Long empresaId;

    public static LoginResponse ok(String token, Long userId, String userName, String rol, Long empresaId) {
        LoginResponse r = new LoginResponse();
        r.ok = true;
        r.token = token;
        r.userId = userId;
        r.userName = userName;
        r.rol = rol;
        r.empresaId = empresaId;
        return r;
    }

    public static LoginResponse fail(String msg) {
        LoginResponse r = new LoginResponse();
        r.ok = false;
        r.error = msg;
        return r;
    }

    public boolean isOk() { return ok; }
    public String getError() { return error; }

    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getRol() { return rol; }
    public Long getEmpresaId() { return empresaId; }
}