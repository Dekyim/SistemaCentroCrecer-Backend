package tip.java.sistemacentrocrecer.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.AuthService;
import tip.java.sistemacentrocrecer.dto.LoginRequestDTO;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private static final String MSG_DESACTIVADA = "Tu cuenta ha sido desactivada";

    @PostMapping("/funcionario/login")
    public ResponseEntity<?> loginFuncionario(@RequestBody LoginRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.loginFuncionario(request));
        } catch (RuntimeException e) {
            int status = e.getMessage().contains(MSG_DESACTIVADA) ? 403 : 401;
            return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/responsable/login")
    public ResponseEntity<?> loginResponsable(@RequestBody LoginRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.loginResponsable(request));
        } catch (RuntimeException e) {
            int status = e.getMessage().contains(MSG_DESACTIVADA) ? 403 : 401;
            return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
        }
    }

}