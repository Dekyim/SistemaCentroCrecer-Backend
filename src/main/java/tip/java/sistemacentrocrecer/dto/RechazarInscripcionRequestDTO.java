package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RechazarInscripcionRequestDTO {

    @NotBlank(message = "El motivo de rechazo es obligatorio")
    private String motivo;
}