package tip.java.sistemacentrocrecer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteGrupoRequestDTO {
    @NotNull(message = "El id del reporte no puede ser nulo")
    private Integer reporte_id;

    @NotNull(message = "El id del grupo no puede ser nulo")
    private Integer grupo_id;

}
