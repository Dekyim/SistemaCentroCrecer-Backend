package tip.java.sistemacentrocrecer.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String COOKIE_NAME = "centrocrecer_token";

    @Value("${app.jwt.secret}")
    private String secret;

    private static final long EXPIRATION_MS = 86400000; // 24 horas

    private Key key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generarToken(String correo, String rol) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerEmail(String token) {
        return parsear(token).getBody().getSubject();
    }

    public String extraerRol(String token) {
        return (String) parsear(token).getBody().get("rol");
    }

    public boolean esValido(String token) {
        try {
            parsear(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parsear(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
    public Long getExpiracion(String token) {
        return parsear(token)
                .getBody()
                .getExpiration()
                .getTime();
    }

}