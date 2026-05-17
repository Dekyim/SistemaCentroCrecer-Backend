package tip.java.sistemacentrocrecer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioStatsResponseDTO {
    private long niniosTotales;
    private long actividadesTotales;
    private long gruposActivos;
}
