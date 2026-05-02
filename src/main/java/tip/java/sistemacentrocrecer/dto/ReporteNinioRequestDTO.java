package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteNinioRequestDTO {
    @NotNull(message = "El id del reporte no puede ser nulo")
    private Integer reporte_id;

    @NotNull(message = "El id del niño no puede ser nulo")
    private Integer ninio_id;

}
