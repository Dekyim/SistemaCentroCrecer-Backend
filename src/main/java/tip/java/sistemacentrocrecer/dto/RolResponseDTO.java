package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RolResponseDTO {
    @NotBlank(message = "El nombre del rol no puede estar vacío")
    @Size(min = 7, max = 100, message = "El nombre debe tener entre 7 y 100 caracteres")
    private String nombre;

    private Integer padreId;

}
