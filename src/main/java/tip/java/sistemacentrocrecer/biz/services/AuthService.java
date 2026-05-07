package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.LoginRequestDTO;
import tip.java.sistemacentrocrecer.security.JwtUtil;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FuncionarioRepository funcionarioRepo;
    private final ResponsableRepository responsableRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Map<String, String> loginFuncionario(LoginRequestDTO req) {
        var funcionario = funcionarioRepo.findByEmail(req.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(req.getPassword(), funcionario.getContrasenia())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generarToken(funcionario.getEmail(), "FUNCIONARIO");
        return Map.of("token", token, "rol", "FUNCIONARIO", "nombre", funcionario.getNombre());
    }

    public Map<String, String> loginResponsable(LoginRequestDTO req) {
        var responsable = responsableRepo.findByEmail(req.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(req.getPassword(), responsable.getContrasenia())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generarToken(responsable.getEmail(), "RESPONSABLE");
        return Map.of("token", token, "rol", "RESPONSABLE", "nombre", responsable.getNombre());
    }
}
