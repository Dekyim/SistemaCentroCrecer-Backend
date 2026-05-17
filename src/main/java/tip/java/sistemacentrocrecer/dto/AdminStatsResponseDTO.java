package tip.java.sistemacentrocrecer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponseDTO {
    private long funcionariosActivos;
    private long funcionariosTotales;
    private long niniosTotales;
    private long actividadesTotales;
    private long turnosActivos;
    private long gruposActivos;
    private int coberturaPorcentaje;
}
