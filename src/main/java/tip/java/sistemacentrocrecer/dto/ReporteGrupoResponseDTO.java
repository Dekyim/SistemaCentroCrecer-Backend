package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteGrupoResponseDTO {
    @NotNull(message = "El id del reporte no puede ser nulo")
    private Integer reporteId;

    @NotNull(message = "El id del grupo no puede ser nulo")
    private Integer grupoId;

}
