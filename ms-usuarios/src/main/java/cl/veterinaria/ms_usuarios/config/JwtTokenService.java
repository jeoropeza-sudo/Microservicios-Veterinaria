package cl.veterinaria.ms_usuarios.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenService {
    private final SecretKey signingKey;

    public JwtTokenService(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes.");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public UsuarioAutenticado validar(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Number id = claims.get("id", Number.class);
        String rol = claims.get("rol", String.class);
        if (id == null || claims.getSubject() == null || rol == null) {
            throw new IllegalArgumentException("El token no contiene la identidad requerida.");
        }
        return new UsuarioAutenticado(id.intValue(), claims.getSubject(), rol);
    }
}
