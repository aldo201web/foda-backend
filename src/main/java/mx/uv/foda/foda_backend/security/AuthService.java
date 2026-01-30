package mx.uv.foda.foda_backend.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final Map<String, AuthSession> sessions = new ConcurrentHashMap<>();

    public String createToken(AuthSession session) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, session);
        return token;
    }

    public AuthSession getSession(String token) {
        return sessions.get(token);
    }

    public void logout(String token) {
        sessions.remove(token);
    }

    public static String extractBearer(String authHeader) {
        if (authHeader == null) return null;
        // "Bearer abc..."
        if (!authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7).trim();
    }
}