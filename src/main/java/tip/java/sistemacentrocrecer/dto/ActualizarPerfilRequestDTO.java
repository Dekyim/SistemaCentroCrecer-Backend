package tip.java.sistemacentrocrecer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActualizarPerfilRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 100)
    private String apellido;

    @Email(message = "Email inválido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Teléfono inválido")
    private String telefono;

    @JsonProperty("fechaNacimiento")
    private LocalDate fechaNacimiento;

    // URL de la foto de perfil (Cloudinary)
    private String fotoPerfil;
}