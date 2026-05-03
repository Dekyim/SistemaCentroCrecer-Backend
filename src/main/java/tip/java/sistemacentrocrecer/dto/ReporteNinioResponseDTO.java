package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteNinioResponseDTO {
    @NotNull(message = "El id del reporte no puede ser nulo")
    private Integer reporteId;

    @NotNull(message = "El id del niño no puede ser nulo")
    private Integer ninioId;

}
