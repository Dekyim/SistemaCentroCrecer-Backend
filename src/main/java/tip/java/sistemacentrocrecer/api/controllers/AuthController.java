package tip.java.sistemacentrocrecer.api.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.AuthService;
import tip.java.sistemacentrocrecer.dto.LoginRequestDTO;
import tip.java.sistemacentrocrecer.dto.LoginResponseDTO;
import tip.java.sistemacentrocrecer.security.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private static final String MSG_DESACTIVADA = "Tu cuenta ha sido desactivada";

    // En prod (HTTPS) debe quedar en true. Solo se baja a false para probar
    // en local por HTTP plano, vía la variable de entorno JWT_COOKIE_SECURE.
    @Value("${app.jwt.cookie-secure:true}")
    private boolean cookieSecure;

    @PostMapping("/funcionario/login")
    public ResponseEntity<?> loginFuncionario(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        try {
            LoginResponseDTO body = authService.loginFuncionario(request);
            setCookieToken(response, body.getToken(), body.getExpiracion());
            body.setToken(null); // el JWT viaja solo en la cookie httpOnly, nunca en el body
            return ResponseEntity.ok(body);
        } catch (RuntimeException e) {
            int status = e.getMessage().contains(MSG_DESACTIVADA) ? 403 : 401;
            return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/responsable/login")
    public ResponseEntity<?> loginResponsable(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        try {
            LoginResponseDTO body = authService.loginResponsable(request);
            setCookieToken(response, body.getToken(), body.getExpiracion());
            body.setToken(null);
            return ResponseEntity.ok(body);
        } catch (RuntimeException e) {
            int status = e.getMessage().contains(MSG_DESACTIVADA) ? 403 : 401;
            return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(JwtUtil.COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada"));
    }

    private void setCookieToken(HttpServletResponse response, String token, Long expiracionEpochMs) {
        long maxAgeSegundos = Math.max(0, (expiracionEpochMs - System.currentTimeMillis()) / 1000);
        ResponseCookie cookie = ResponseCookie.from(JwtUtil.COOKIE_NAME, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAgeSegundos)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}