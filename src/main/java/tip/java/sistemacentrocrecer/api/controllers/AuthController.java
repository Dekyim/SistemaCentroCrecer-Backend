package tip.java.sistemacentrocrecer.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tip.java.sistemacentrocrecer.biz.services.AuthService;
import tip.java.sistemacentrocrecer.dto.LoginRequestDTO;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/funcionario/login")
    public ResponseEntity<?> loginFuncionario(@RequestBody LoginRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.loginFuncionario(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/responsable/login")
    public ResponseEntity<?> loginResponsable(@RequestBody LoginRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.loginResponsable(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

}
