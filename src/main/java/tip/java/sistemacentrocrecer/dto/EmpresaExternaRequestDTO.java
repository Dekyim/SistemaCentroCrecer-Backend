package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmpresaExternaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipoServicio es obligatorio")
    private String tipoServicio;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$")
    private String telefono;
}
