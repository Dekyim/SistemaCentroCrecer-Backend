package tip.java.sistemacentrocrecer.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jdk.jfr.Category;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "centrocrecer-clave-super-secreta-2024";
    private static final long EXPIRATION_MS = 86400000; // 24 horas

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generarToken(String correo, String rol) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerCorreo(String token) {
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
}
