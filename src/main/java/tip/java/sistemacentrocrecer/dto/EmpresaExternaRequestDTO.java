package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmpresaExternaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El tipoServicio es obligatorio")
    @Size(max = 100)
    private String tipoServicio;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Formato de teléfono inválido")
    private String telefono;

    @NotNull(message = "La actividad es obligatoria")
    private Integer actividadId;;
}
