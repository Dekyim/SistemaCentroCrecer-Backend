package tip.java.sistemacentrocrecer.biz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tip.java.sistemacentrocrecer.biz.dao.repositories.FuncionarioRepository;
import tip.java.sistemacentrocrecer.biz.dao.repositories.ResponsableRepository;
import tip.java.sistemacentrocrecer.dto.LoginRequestDTO;
import tip.java.sistemacentrocrecer.dto.LoginResponseDTO;
import tip.java.sistemacentrocrecer.security.JwtUtil;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final FuncionarioRepository funcionarioRepo;
    private final ResponsableRepository responsableRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO loginFuncionario(LoginRequestDTO req) {
        var funcionario = funcionarioRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(req.getContrasenia(), funcionario.getContrasenia())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!funcionario.isActivo()) {
            throw new RuntimeException("La cuenta está inactiva. Contacte al administrador.");
        }

        String rolNombre = funcionario.getRol().getNombre();
        String token = jwtUtil.generarToken(funcionario.getEmail(), rolNombre);

        return LoginResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .rol(rolNombre)
                .id(Long.valueOf(funcionario.getId()))
                .nombreCompleto(funcionario.getNombre() + " " + funcionario.getApellido())
                .email(funcionario.getEmail())
                .expiracion(jwtUtil.getExpiracion(token))
                .mustChangePassword(funcionario.getMustChangePassword() != null && funcionario.getMustChangePassword())
                .fotoPerfil(funcionario.getFotoPerfil())
                .build();
    }

    public LoginResponseDTO loginResponsable(LoginRequestDTO req) {
        var responsable = responsableRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(req.getContrasenia(), responsable.getContrasenia())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generarToken(responsable.getEmail(), "RESPONSABLE");

        return LoginResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .rol("RESPONSABLE")
                .id(Long.valueOf(responsable.getId()))
                .nombreCompleto(responsable.getNombre())
                .email(responsable.getEmail())
                .expiracion(jwtUtil.getExpiracion(token))
                .mustChangePassword(false)
                .build();
    }

}