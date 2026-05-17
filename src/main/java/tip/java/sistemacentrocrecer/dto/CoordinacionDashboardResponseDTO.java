package tip.java.sistemacentrocrecer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinacionDashboardResponseDTO {

    private List<AgendaResponseDTO> eventosDelDia;
    private long totalEventosDelDia;

    private List<ActividadResponseDTO> actividadesActivas;
    private long totalActividadesActivas;

    private List<AgendaResponseDTO> conflictosAgenda;
    private long totalConflictos;

    private List<AgendaLimpiezaResponseDTO> limpiezasPendientes;
    private long totalLimpiezasPendientes;

    private List<ActividadResponseDTO> actividadesProximas;
    private long totalActividadesProximas;
}
