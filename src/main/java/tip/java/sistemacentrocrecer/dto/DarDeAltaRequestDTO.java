package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DarDeAltaRequestDTO {

    @NotNull(message = "El grupoId es obligatorio")
    private Integer grupoId;

    private String observaciones;
}