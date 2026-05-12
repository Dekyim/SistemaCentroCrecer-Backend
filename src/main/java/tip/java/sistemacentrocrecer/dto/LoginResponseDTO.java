package tip.java.sistemacentrocrecer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipoToken;      // "Bearer"
    private String rol;            // "FUNCIONARIO" o "RESPONSABLE"
    private Long id;               // id del usuario autenticado
    private String nombreCompleto; // para mostrar en el header de la app
    private String email;         // para confirmar quién inició sesión
    private Long expiracion;       // timestamp cuando vence el token
}
